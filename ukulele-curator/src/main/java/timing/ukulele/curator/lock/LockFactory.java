package timing.ukulele.curator.lock;


import org.apache.curator.framework.CuratorFramework;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import timing.ukulele.curator.annotation.ZookeeperLock;
import timing.ukulele.curator.core.LockInfoProvider;
import timing.ukulele.curator.model.LockInfo;
import timing.ukulele.curator.model.LockType;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

public class LockFactory {
    private Logger logger= LoggerFactory.getLogger(getClass());

    @Autowired
    private CuratorFramework curatorClient;

    @Autowired
    private LockInfoProvider lockInfoProvider;

    private static final Map<LockType,Lock> lockMap= new HashMap<>();

    @PostConstruct
    public void init(){
        lockMap.put(LockType.Reentrant,new ReentrantLock(curatorClient));
        lockMap.put(LockType.Share,new ShareLock(curatorClient));
        lockMap.put(LockType.Read,new ReadLock(curatorClient));
        lockMap.put(LockType.Write,new WriteLock(curatorClient));
        logger.info("ZookeeperLock Initialization Successful");
    }

    public Lock getLock(ProceedingJoinPoint joinPoint, ZookeeperLock timingLock){
        LockInfo lockInfo = lockInfoProvider.get(joinPoint,timingLock);
        return lockMap.get(lockInfo.getType()).setLockInfo(lockInfo);
    }
}

