package timing.ukulele.redisson.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import timing.ukulele.redisson.lock.model.LockInfo;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 基于Redis的Redisson分布式可重入公平锁也是实现了java.util.concurrent.locks.Lock接口的一种RLock对象。
 * 它保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。
 * 如果负责储存这个分布式锁的Redis节点宕机以后，而且这个锁正好处于锁住的状态时，这个锁会出现锁死的状态。
 * 为了避免这种情况的发生，Redisson内部提供了一个监控锁的看门狗，它的作用是在Redisson实例被关闭前，不断的延长锁的有效期。
 * 默认情况下，看门狗的检查锁的超时时间是30秒钟，也可以通过修改Config.lockWatchdogTimeout来另行指定。
 * 另外Redisson还通过加锁的方法提供了leaseTime的参数来指定加锁的时间。超过这个时间后锁便自动解开了。
 */
public class FairLock implements Lock {

    private RLock rLock;

    private final LockInfo lockInfo;

    private RedissonClient redissonClient;

    public FairLock(RedissonClient redissonClient, LockInfo info) {
        this.redissonClient = redissonClient;
        this.lockInfo = info;
    }

    @Override
    public boolean acquire() {
        try {
            rLock = redissonClient.getFairLock(lockInfo.getName());
            return rLock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public boolean release() {
        if (rLock.isHeldByCurrentThread()) {
            try {
                return rLock.forceUnlockAsync().get();
            } catch (InterruptedException | ExecutionException e) {
                return false;
            }
        }
        return false;
    }

}
