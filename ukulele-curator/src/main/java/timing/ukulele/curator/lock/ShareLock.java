package timing.ukulele.curator.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import timing.ukulele.curator.model.LockInfo;

import java.util.concurrent.TimeUnit;

import static org.apache.curator.framework.state.ConnectionState.LOST;
import static org.apache.curator.framework.state.ConnectionState.SUSPENDED;

public class ShareLock implements Lock ,ConnectionStateListener {

    private InterProcessLock shareLock;

    private LockInfo lockInfo;

    private CuratorFramework curatorClient;

    public ShareLock(CuratorFramework curatorClient) {
        this.curatorClient = curatorClient;
    }

    @Override
    public boolean acquire()  {
        try {
            shareLock = new InterProcessSemaphoreMutex(curatorClient, lockInfo.getName());
            return shareLock.acquire(lockInfo.getWaitTime(), TimeUnit.SECONDS);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void release() throws Exception {
        if (shareLock.isAcquiredInThisProcess()) {
            shareLock.release();
        }
    }
    @Override
    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
        if(LOST==connectionState||SUSPENDED==connectionState)
            try {
                release();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public Lock setLockInfo(LockInfo lockInfo) {
        this.lockInfo = lockInfo;
        return this;
    }
}
