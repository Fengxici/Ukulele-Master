package timing.ukulele.curator.core;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import timing.ukulele.curator.annotation.ZookeeperLock;
import timing.ukulele.curator.lock.Lock;
import timing.ukulele.curator.lock.LockFactory;
/**
 * 给添加@TimingLock切面加锁处理
 */
@Aspect
@Component
public class ZookeeperLockAspectHandler {
    @Autowired
    LockFactory lockFactory;

    private ThreadLocal<Lock> currentThreadLock = new ThreadLocal<>();
    private ThreadLocal<Boolean> currentThreadLockRes = new ThreadLocal<>();

    @Around(value = "@annotation(zookeeperLock)")
    public Object around(ProceedingJoinPoint joinPoint, ZookeeperLock zookeeperLock) throws Throwable {
        Lock lock = lockFactory.getLock(joinPoint, zookeeperLock);
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


    @AfterReturning(value = "@annotation(zookeeperLock)")
    public void afterReturning(ZookeeperLock zookeeperLock) {
        if (currentThreadLockRes.get()) {
            try {
                currentThreadLock.get().release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @AfterThrowing(value = "@annotation(zookeeperLock)")
    public void afterThrowing(ZookeeperLock zookeeperLock) throws Exception {
        if (currentThreadLockRes.get()) {
            currentThreadLock.get().release();
        }
    }
}

