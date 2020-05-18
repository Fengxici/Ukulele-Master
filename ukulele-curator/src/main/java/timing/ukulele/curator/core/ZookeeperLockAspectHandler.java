package timing.ukulele.curator.core;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import timing.ukulele.curator.annotation.ZookeeperLock;
import timing.ukulele.curator.handler.ZookeeperLockInvocationException;
import timing.ukulele.curator.lock.Lock;
import timing.ukulele.curator.lock.LockFactory;
import timing.ukulele.curator.model.LockInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 给添加@ZookeeperLock切面加锁处理
 *
 * @author fengxici
 */
@Aspect
@Component
public class ZookeeperLockAspectHandler {
    private final LockFactory lockFactory;
    private final LockInfoProvider lockInfoProvider;
    private ThreadLocal<Lock> currentThreadLock = new ThreadLocal<>();
    private ThreadLocal<LockRes> currentThreadLockRes = new ThreadLocal<>();
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperLockAspectHandler.class);

    @Autowired
    public ZookeeperLockAspectHandler(LockFactory lockFactory, LockInfoProvider lockInfoProvider) {
        this.lockFactory = lockFactory;
        this.lockInfoProvider = lockInfoProvider;
    }

    @Around(value = "@annotation(zookeeperLock)")
    public Object around(ProceedingJoinPoint joinPoint, ZookeeperLock zookeeperLock) throws Throwable {
        LockInfo lockInfo = lockInfoProvider.get(joinPoint, zookeeperLock);
        lockInfo.setLeaseTime(System.currentTimeMillis());
        currentThreadLockRes.set(new LockRes(lockInfo, false));
        Lock lock = lockFactory.getLock(lockInfo);
        boolean lockRes = lock.acquire();
        if (!lockRes) {
            if (logger.isWarnEnabled()) {
                logger.warn("Timeout while acquiring Lock({})", lockInfo.getName());
            }
            if (!StringUtils.isEmpty(zookeeperLock.customLockTimeoutStrategy())) {
                return handleCustomLockTimeout(zookeeperLock.customLockTimeoutStrategy(), joinPoint);
            } else {
                lockInfo.setLeaseTime((System.currentTimeMillis() - lockInfo.getLeaseTime()) / 1000);
                zookeeperLock.lockTimeoutStrategy().handle(lockInfo, lock, joinPoint);
            }
        }
        currentThreadLock.set(lock);
        currentThreadLockRes.get().setRes(true);
        return joinPoint.proceed();
    }


    @AfterReturning(value = "@annotation(zookeeperLock)")
    public void afterReturning(JoinPoint joinPoint, ZookeeperLock zookeeperLock) throws Throwable {
        releaseLock(zookeeperLock, joinPoint);
        cleanUpThreadLocal();
    }

    @AfterThrowing(value = "@annotation(zookeeperLock)", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, ZookeeperLock zookeeperLock, Throwable ex) throws Throwable {
        releaseLock(zookeeperLock, joinPoint);
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
            throw new ZookeeperLockInvocationException("Fail to invoke custom lock timeout handler: " + lockTimeoutHandler, e);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }

        return res;
    }

    /**
     * 释放锁
     */
    private void releaseLock(ZookeeperLock zookeeperLock, JoinPoint joinPoint) throws Throwable {
        LockRes lockRes = currentThreadLockRes.get();
        if (lockRes.getRes()) {
            boolean releaseRes = currentThreadLock.get().release();
            // avoid release lock twice when exception happens below
            lockRes.setRes(false);
            if (!releaseRes) {
                handleReleaseTimeout(zookeeperLock, lockRes.getLockInfo(), joinPoint);
            }
        }
    }


    /**
     * 处理释放锁时已超时
     */
    private void handleReleaseTimeout(ZookeeperLock zookeeperLock, LockInfo lockInfo, JoinPoint joinPoint) throws Throwable {

        if (logger.isWarnEnabled()) {
            logger.warn("Timeout while release Lock({})", lockInfo.getName());
        }

        if (!StringUtils.isEmpty(zookeeperLock.customReleaseTimeoutStrategy())) {

            handleCustomReleaseTimeout(zookeeperLock.customReleaseTimeoutStrategy(), joinPoint);

        } else {
            zookeeperLock.releaseTimeoutStrategy().handle(lockInfo);
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
            throw new ZookeeperLockInvocationException("Fail to invoke custom release timeout handler: " + releaseTimeoutHandler, e);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    private static class LockRes {

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

    /**
     * avoid memory leak
     */
    private void cleanUpThreadLocal() {

        currentThreadLockRes.remove();
        currentThreadLock.remove();
    }

}

