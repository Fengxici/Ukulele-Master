package timing.ukulele.redisson.lock.core;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import timing.ukulele.redisson.lock.annotation.RedisLock;
import timing.ukulele.redisson.lock.config.RedisLockConfig;
import timing.ukulele.redisson.lock.model.LockInfo;
import timing.ukulele.redisson.lock.model.LockType;

/**
 * @author fengxici
 */
public class LockInfoProvider {

    public static final String LOCK_NAME_PREFIX = "lock";
    public static final String LOCK_NAME_SEPARATOR = ".";


    @Autowired
    private RedisLockConfig redisLockConfig;

    @Autowired
    private BusinessKeyProvider businessKeyProvider;

    private static final Logger logger = LoggerFactory.getLogger(LockInfoProvider.class);

    public LockInfo get(ProceedingJoinPoint joinPoint, RedisLock redisLock) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        LockType type = redisLock.lockType();
        String businessKeyName = businessKeyProvider.getKeyName(joinPoint, redisLock);
        String lockName = LOCK_NAME_PREFIX + LOCK_NAME_SEPARATOR + getName(redisLock.name(), signature) + businessKeyName;
        long waitTime = getWaitTime(redisLock);
        long leaseTime = getLeaseTime(redisLock);

        if (leaseTime == -1 && logger.isWarnEnabled()) {
            logger.warn("Trying to acquire Lock({}) with no expiration, " +
                    "RedisLock will keep prolong the lock expiration while the lock is still holding by current thread. " +
                    "This may cause dead lock in some circumstances.", lockName);
        }

        return new LockInfo(type, lockName, waitTime, leaseTime);
    }

    private String getName(String annotationName, MethodSignature signature) {
        if (annotationName.isEmpty()) {
            return String.format("%s.%s", signature.getDeclaringTypeName(), signature.getMethod().getName());
        } else {
            return annotationName;
        }
    }


    private long getWaitTime(RedisLock lock) {
        return lock.waitTime() == Long.MIN_VALUE ?
                redisLockConfig.getWaitTime() : lock.waitTime();
    }

    private long getLeaseTime(RedisLock lock) {
        return lock.leaseTime() == Long.MIN_VALUE ?
                redisLockConfig.getLeaseTime() : lock.leaseTime();
    }
}
