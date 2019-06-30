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

public class ShareLock implements Lock, ConnectionStateListener {

    private InterProcessLock shareLock;

    private LockInfo lockInfo;

    private CuratorFramework curatorClient;

    public ShareLock(CuratorFramework curatorClient, LockInfo info) {
        this.curatorClient = curatorClient;
        this.lockInfo = info;
    }

    @Override
    public boolean acquire() {
        try {
            shareLock = new InterProcessSemaphoreMutex(curatorClient, lockInfo.getName());
            return shareLock.acquire(lockInfo.getWaitTime(), TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean release() {
        if (shareLock.isAcquiredInThisProcess()) {
            try {
                shareLock.release();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
        if (LOST == connectionState || SUSPENDED == connectionState)
            release();
    }
}
