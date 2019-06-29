package timing.ukulele.curator.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import timing.ukulele.curator.model.LockInfo;

import java.util.concurrent.TimeUnit;

import static org.apache.curator.framework.state.ConnectionState.LOST;
import static org.apache.curator.framework.state.ConnectionState.SUSPENDED;

public class WriteLock implements Lock ,ConnectionStateListener {

    private InterProcessReadWriteLock interProcessReadWriteLock;

    private LockInfo lockInfo;

    private CuratorFramework curatorClient;

    public WriteLock(CuratorFramework curatorClient) {
        this.curatorClient = curatorClient;
    }

    @Override
    public boolean acquire() {
        try {
            interProcessReadWriteLock = new InterProcessReadWriteLock(curatorClient, lockInfo.getName());
            return interProcessReadWriteLock.writeLock().acquire(lockInfo.getLeaseTime(), TimeUnit.SECONDS);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void release() throws Exception {
        if(interProcessReadWriteLock.writeLock().isAcquiredInThisProcess()){
            interProcessReadWriteLock.writeLock().release();
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
    public LockInfo getLockInfo() {
        return lockInfo;
    }

    public Lock setLockInfo(LockInfo lockInfo) {
        this.lockInfo = lockInfo;
        return this;
    }
}

