package timing.ukulele.redisson.lock.core;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import timing.ukulele.redisson.lock.annotation.RedisLock;
import timing.ukulele.redisson.lock.handler.RedisLockInvocationException;
import timing.ukulele.redisson.lock.Lock;
import timing.ukulele.redisson.lock.LockFactory;
import timing.ukulele.redisson.lock.model.LockInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 给添加@TRedisLock切面加锁处理
 */
@Aspect
@Component
public class RedisLockAspectHandler {
    private static final Logger logger = LoggerFactory.getLogger(RedisLockAspectHandler.class);

    private final LockFactory lockFactory;

    private final LockInfoProvider lockInfoProvider;

    private ThreadLocal<Lock> currentThreadLock = new ThreadLocal<>();
    private ThreadLocal<LockRes> currentThreadLockRes = new ThreadLocal<>();

    @Autowired
    public RedisLockAspectHandler(LockFactory lockFactory, LockInfoProvider lockInfoProvider) {
        this.lockFactory = lockFactory;
        this.lockInfoProvider = lockInfoProvider;
    }

    @Around(value = "@annotation(redisLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
        LockInfo lockInfo = lockInfoProvider.get(joinPoint, redisLock);
        currentThreadLockRes.set(new LockRes(lockInfo, false));
        Lock lock = lockFactory.getLock(lockInfo);
        boolean lockRes = lock.acquire();

        if (!lockRes) {
            if (logger.isWarnEnabled()) {
                logger.warn("Timeout while acquiring Lock({})", lockInfo.getName());
            }

            if (!StringUtils.isEmpty(redisLock.customLockTimeoutStrategy())) {

                return handleCustomLockTimeout(redisLock.customLockTimeoutStrategy(), joinPoint);

            } else {
                redisLock.lockTimeoutStrategy().handle(lockInfo, lock, joinPoint);
            }
        }

        currentThreadLock.set(lock);
        currentThreadLockRes.get().setRes(true);

        return joinPoint.proceed();
    }

    @AfterReturning(value = "@annotation(redisLock)")
    public void afterReturning(JoinPoint joinPoint, RedisLock redisLock) throws Throwable {

        releaseLock(redisLock, joinPoint);
        cleanUpThreadLocal();
    }

    @AfterThrowing(value = "@annotation(redisLock)", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, RedisLock redisLock, Throwable ex) throws Throwable {

        releaseLock(redisLock, joinPoint);
        cleanUpThreadLocal();
        throw ex;
    }

    /**
     * 处理自定义加锁超时
     */
    private Object handleCustomLockTimeout(String lockTimeoutHandler, JoinPoint joinPoint) throws Throwable {

        // prepare invocation context
        Method currentMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object target = joinPoint.getTarget();
        Method handleMethod = null;
        try {
            handleMethod = joinPoint.getTarget().getClass().getDeclaredMethod(lockTimeoutHandler, currentMethod.getParameterTypes());
            handleMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Illegal annotation param customLockTimeoutStrategy", e);
        }
        Object[] args = joinPoint.getArgs();

        // invoke
        Object res = null;
        try {
            res = handleMethod.invoke(target, args);
        } catch (IllegalAccessException e) {
            throw new RedisLockInvocationException("Fail to invoke custom lock timeout handler: " + lockTimeoutHandler, e);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }

        return res;
    }

    /**
     * 释放锁
     */
    private void releaseLock(RedisLock redisLock, JoinPoint joinPoint) throws Throwable {
        LockRes lockRes = currentThreadLockRes.get();
        if (lockRes.getRes()) {
            boolean releaseRes = currentThreadLock.get().release();
            // avoid release lock twice when exception happens below
            lockRes.setRes(false);
            if (!releaseRes) {
                handleReleaseTimeout(redisLock, lockRes.getLockInfo(), joinPoint);
            }
        }
    }


    /**
     * 处理释放锁时已超时
     */
    private void handleReleaseTimeout(RedisLock redisLock, LockInfo lockInfo, JoinPoint joinPoint) throws Throwable {

        if (logger.isWarnEnabled()) {
            logger.warn("Timeout while release Lock({})", lockInfo.getName());
        }

        if (!StringUtils.isEmpty(redisLock.customReleaseTimeoutStrategy())) {

            handleCustomReleaseTimeout(redisLock.customReleaseTimeoutStrategy(), joinPoint);

        } else {
            redisLock.releaseTimeoutStrategy().handle(lockInfo);
        }

    }

    /**
     * 处理自定义释放锁时已超时
     */
    private void handleCustomReleaseTimeout(String releaseTimeoutHandler, JoinPoint joinPoint) throws Throwable {

        Method currentMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object target = joinPoint.getTarget();
        Method handleMethod = null;
        try {
            handleMethod = joinPoint.getTarget().getClass().getDeclaredMethod(releaseTimeoutHandler, currentMethod.getParameterTypes());
            handleMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Illegal annotation param customReleaseTimeoutStrategy", e);
        }
        Object[] args = joinPoint.getArgs();

        try {
            handleMethod.invoke(target, args);
        } catch (IllegalAccessException e) {
            throw new RedisLockInvocationException("Fail to invoke custom release timeout handler: " + releaseTimeoutHandler, e);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    private class LockRes {

        private LockInfo lockInfo;

        private Boolean res;

        LockRes(LockInfo lockInfo, Boolean res) {
            this.lockInfo = lockInfo;
            this.res = res;
        }

        LockInfo getLockInfo() {
            return lockInfo;
        }

        Boolean getRes() {
            return res;
        }

        void setRes(Boolean res) {
            this.res = res;
        }

        void setLockInfo(LockInfo lockInfo) {
            this.lockInfo = lockInfo;
        }
    }

    // avoid memory leak
    private void cleanUpThreadLocal() {

        currentThreadLockRes.remove();
        currentThreadLock.remove();
    }
}
