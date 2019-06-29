package timing.ukulele.curator.lock;

import timing.ukulele.curator.model.LockInfo;

public interface Lock {
    Lock setLockInfo(LockInfo lockInfo);

    boolean acquire() throws Exception;

    void release() throws Exception;
}
