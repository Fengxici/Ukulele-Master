package timing.ukulele.redisson.lock;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import timing.ukulele.redisson.lock.model.LockInfo;

public class LockFactory {

    @Autowired
    private RedissonClient redissonClient;

    public Lock getLock(LockInfo lockInfo) {
        switch (lockInfo.getType()) {
            case Reentrant:
                return new ReentrantLock(redissonClient, lockInfo);
            case Fair:
                return new FairLock(redissonClient, lockInfo);
            case Read:
                return new ReadLock(redissonClient, lockInfo);
            case Write:
                return new WriteLock(redissonClient, lockInfo);
            default:
                return new ReentrantLock(redissonClient, lockInfo);
        }
    }
}
