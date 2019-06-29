package timing.ukulele.curator.core;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import timing.ukulele.curator.annotation.ZookeeperLock;
import timing.ukulele.curator.config.ZookeeperLockConfig;
import timing.ukulele.curator.model.LockInfo;
import timing.ukulele.curator.model.LockType;

public class LockInfoProvider {
    public static final String LOCK_NAME_PREFIX = "lock";
    public static final String LOCK_NAME_SEPARATOR = ".";


    @Autowired
    private ZookeeperLockConfig timingLockConfig;

    @Autowired
    private BusinessKeyProvider businessKeyProvider;

    public LockInfo get(ProceedingJoinPoint joinPoint, ZookeeperLock lock) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        LockType type= lock.lockType();
        String businessKeyName=businessKeyProvider.getKeyName(joinPoint,lock);
        String lockName = LOCK_NAME_PREFIX+LOCK_NAME_SEPARATOR+getName(lock.name(), signature)+businessKeyName;
        long waitTime = getWaitTime(lock);
        long leaseTime = getLeaseTime(lock);
        return new LockInfo(type,lockName,waitTime,leaseTime);
    }

    private String getName(String annotationName, MethodSignature signature) {
        if (annotationName.isEmpty()) {
            return String.format("%s.%s", signature.getDeclaringTypeName(), signature.getMethod().getName());
        } else {
            return annotationName;
        }
    }


    private long getWaitTime(ZookeeperLock lock) {
        return lock.waitTime() == Long.MIN_VALUE ?
                timingLockConfig.getWaitTime() : lock.waitTime();
    }

    private long getLeaseTime(ZookeeperLock lock) {
        return lock.leaseTime() == Long.MIN_VALUE ?
                timingLockConfig.getLeaseTime() : lock.leaseTime();
    }
}
