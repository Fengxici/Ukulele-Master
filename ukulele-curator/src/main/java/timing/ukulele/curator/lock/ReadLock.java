package timing.ukulele.curator.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import timing.ukulele.curator.model.LockInfo;

import java.util.concurrent.TimeUnit;

import static org.apache.curator.framework.state.ConnectionState.LOST;
import static org.apache.curator.framework.state.ConnectionState.SUSPENDED;

public class ReadLock implements Lock,ConnectionStateListener {

    private InterProcessReadWriteLock interProcessReadWriteLock;

    private LockInfo lockInfo;

    private CuratorFramework curatorClient;

    public ReadLock(CuratorFramework curatorClient) {
        this.curatorClient = curatorClient;
    }

    @Override
    public boolean acquire() {
        try {
            interProcessReadWriteLock = new InterProcessReadWriteLock(curatorClient, lockInfo.getName());
            return interProcessReadWriteLock.readLock().acquire(lockInfo.getWaitTime(), TimeUnit.SECONDS);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void release() throws Exception {
        if(interProcessReadWriteLock.readLock().isAcquiredInThisProcess()){
            interProcessReadWriteLock.readLock().release();
        }
    }
    public LockInfo getLockInfo() {
        return lockInfo;
    }

    public Lock setLockInfo(LockInfo lockInfo) {
        this.lockInfo = lockInfo;
        return this;

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
}

