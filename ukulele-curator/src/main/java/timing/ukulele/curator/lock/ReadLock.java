package timing.ukulele.curator.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import timing.ukulele.curator.model.LockInfo;

import java.util.concurrent.TimeUnit;

import static org.apache.curator.framework.state.ConnectionState.LOST;
import static org.apache.curator.framework.state.ConnectionState.SUSPENDED;

/**
 * @author fengxici
 */
public class ReadLock implements Lock, ConnectionStateListener {

    private InterProcessReadWriteLock interProcessReadWriteLock;

    private LockInfo lockInfo;

    private CuratorFramework curatorClient;

    public ReadLock(CuratorFramework curatorClient, LockInfo info) {
        this.curatorClient = curatorClient;
        this.lockInfo = info;
    }

    @Override
    public boolean acquire() {
        try {
            interProcessReadWriteLock = new InterProcessReadWriteLock(curatorClient, lockInfo.getName());
            return interProcessReadWriteLock.readLock().acquire(lockInfo.getWaitTime(), TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean release() {
        if (interProcessReadWriteLock.readLock().isAcquiredInThisProcess()) {
            try {
                interProcessReadWriteLock.readLock().release();
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

