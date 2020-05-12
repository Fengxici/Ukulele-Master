package timing.ukulele.curator.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import timing.ukulele.curator.model.LockInfo;

import java.util.concurrent.TimeUnit;

import static org.apache.curator.framework.state.ConnectionState.LOST;
import static org.apache.curator.framework.state.ConnectionState.SUSPENDED;

public class ReentrantLock implements Lock, ConnectionStateListener {

    private InterProcessLock reentrantLock;

    private LockInfo lockInfo;

    private CuratorFramework curatorClient;

    public ReentrantLock(CuratorFramework curatorClient, LockInfo info) {
        this.curatorClient = curatorClient;
        this.lockInfo = info;
    }

    @Override
    public boolean acquire() {
        try {
            reentrantLock = new InterProcessMutex(curatorClient, lockInfo.getName());
            return reentrantLock.acquire(lockInfo.getWaitTime(), TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean release() {
        if (reentrantLock.isAcquiredInThisProcess()) {
            try {
                reentrantLock.release();
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
        if (LOST == connectionState || SUSPENDED == connectionState) {
            release();
        }
    }
}
