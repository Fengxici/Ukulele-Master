package timing.ukulele.curator.lock;


import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import timing.ukulele.curator.model.LockInfo;

public class LockFactory {

    @Autowired
    private CuratorFramework curatorClient;

    public Lock getLock(LockInfo lockInfo) {
        switch (lockInfo.getType()) {
            case Reentrant:
                return new ReentrantLock(curatorClient, lockInfo);
            case Share:
                return new ShareLock(curatorClient, lockInfo);
            case Read:
                return new ReadLock(curatorClient, lockInfo);
            case Write:
                return new WriteLock(curatorClient, lockInfo);
            default:
                return new ReentrantLock(curatorClient, lockInfo);
        }
    }
}
