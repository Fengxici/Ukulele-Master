package timing.ukulele.redisson.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import timing.ukulele.redisson.annotation.RedissonLock;
import timing.ukulele.redisson.core.LockInfoProvider;
import timing.ukulele.redisson.model.LockInfo;
import timing.ukulele.redisson.model.LockType;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

public class LockFactory {
    private Logger logger= LoggerFactory.getLogger(getClass());

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private LockInfoProvider lockInfoProvider;

    private static final Map<LockType,Lock> lockMap= new HashMap<>();

    @PostConstruct
    public void init(){
        lockMap.put(LockType.Reentrant,new ReentrantLock(redissonClient));
        lockMap.put(LockType.Fair,new FairLock(redissonClient));
        lockMap.put(LockType.Read,new ReadLock(redissonClient));
        lockMap.put(LockType.Write,new WriteLock(redissonClient));
        logger.info("RedissonLock Initialization Successful");
    }

    public Lock getLock(ProceedingJoinPoint joinPoint, RedissonLock timingLock){
        LockInfo lockInfo = lockInfoProvider.get(joinPoint,timingLock);
        return lockMap.get(lockInfo.getType()).setLockInfo(lockInfo);
    }
}
