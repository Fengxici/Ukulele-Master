package timing.ukulele.redisson.core;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import timing.ukulele.redisson.annotation.RedissonLock;
import timing.ukulele.redisson.lock.Lock;
import timing.ukulele.redisson.lock.LockFactory;

/**
 * 给添加@TimingLock切面加锁处理
 */
@Aspect
@Component
public class RedissonLockAspectHandler {
    @Autowired
    LockFactory lockFactory;

    private ThreadLocal<Lock> currentThreadLock = new ThreadLocal<>();
    private ThreadLocal<Boolean> currentThreadLockRes = new ThreadLocal<>();

    @Around(value = "@annotation(RedissonLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedissonLock redissonLock) throws Throwable {
        Lock lock = lockFactory.getLock(joinPoint,redissonLock);
        boolean currentThreadLock = false;
        try {
            currentThreadLock = lock.acquire();
            return joinPoint.proceed();
        } finally {
            if (currentThreadLock) {
                lock.release();
            }
        }
    }


    @AfterReturning(value = "@annotation(RedissonLock)")
    public void afterReturning(RedissonLock redissonLock) {
        if (currentThreadLockRes.get()) {
            currentThreadLock.get().release();
        }
    }

    @AfterThrowing(value = "@annotation(RedissonLock)")
    public void afterThrowing (RedissonLock redissonLock) {
        if (currentThreadLockRes.get()) {
            currentThreadLock.get().release();
        }
    }
}
