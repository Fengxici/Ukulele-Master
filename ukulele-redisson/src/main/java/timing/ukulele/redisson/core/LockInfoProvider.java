package timing.ukulele.redisson.core;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import timing.ukulele.redisson.annotation.RedissonLock;
import timing.ukulele.redisson.config.RedissonLockConfig;
import timing.ukulele.redisson.model.LockInfo;
import timing.ukulele.redisson.model.LockType;

public class LockInfoProvider {
    public static final String LOCK_NAME_PREFIX = "lock";
    public static final String LOCK_NAME_SEPARATOR = ".";


    @Autowired
    private RedissonLockConfig timingLockConfig;

    @Autowired
    private BusinessKeyProvider businessKeyProvider;

    public LockInfo get(ProceedingJoinPoint joinPoint, RedissonLock lvLock) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        LockType type= lvLock.lockType();
        String businessKeyName=businessKeyProvider.getKeyName(joinPoint,lvLock);
        String lockName = LOCK_NAME_PREFIX+LOCK_NAME_SEPARATOR+getName(lvLock.name(), signature)+businessKeyName;
        long waitTime = getWaitTime(lvLock);
        long leaseTime = getLeaseTime(lvLock);
        return new LockInfo(type,lockName,waitTime,leaseTime);
    }

    private String getName(String annotationName, MethodSignature signature) {
        if (annotationName.isEmpty()) {
            return String.format("%s.%s", signature.getDeclaringTypeName(), signature.getMethod().getName());
        } else {
            return annotationName;
        }
    }


    private long getWaitTime(RedissonLock lock) {
        return lock.waitTime() == Long.MIN_VALUE ?
                timingLockConfig.getWaitTime() : lock.waitTime();
    }

    private long getLeaseTime(RedissonLock lock) {
        return lock.leaseTime() == Long.MIN_VALUE ?
                timingLockConfig.getLeaseTime() : lock.leaseTime();
    }
}
