package timing.ukulele.redisson.lock;

import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import timing.ukulele.redisson.model.LockInfo;

import java.util.concurrent.TimeUnit;

/**
 * 基于Redis的Redisson分布式可重入读写锁RReadWriteLock Java对象实现了java.util.concurrent.locks.ReadWriteLock接口。
 * 同时还支持自动过期解锁。该对象允许同时有多个读取锁，但是最多只能有一个写入锁。
 * 如果负责储存这个分布式锁的Redis节点宕机以后，而且这个锁正好处于锁住的状态时，这个锁会出现锁死的状态。
 * 为了避免这种情况的发生，Redisson内部提供了一个监控锁的看门狗，它的作用是在Redisson实例被关闭前，不断的延长锁的有效期。
 * 默认情况下，看门狗的检查锁的超时时间是30秒钟，也可以通过修改Config.lockWatchdogTimeout来另行指定。
 * 另外Redisson还通过加锁的方法提供了leaseTime的参数来指定加锁的时间。超过这个时间后锁便自动解开了。
 */
public class WriteLock implements Lock {

    private RReadWriteLock rLock;

    private LockInfo lockInfo;

    private RedissonClient redissonClient;

    public WriteLock(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean acquire() {
        try {
            rLock=redissonClient.getReadWriteLock(lockInfo.getName());
            return rLock.writeLock().tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public void release() {
        if(rLock.writeLock().isHeldByCurrentThread()){
            rLock.writeLock().unlockAsync();
        }
    }

    public LockInfo getLockInfo() {
        return lockInfo;
    }

    public Lock setLockInfo(LockInfo lockInfo) {
        this.lockInfo = lockInfo;
        return this;
    }
}
