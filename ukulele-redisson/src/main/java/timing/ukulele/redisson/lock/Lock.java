package timing.ukulele.redisson.lock;

import timing.ukulele.redisson.model.LockInfo;

public interface Lock {
    Lock setLockInfo(LockInfo lockInfo);

    boolean acquire();

    void release();
}
