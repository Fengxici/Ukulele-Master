package timing.ukulele.curator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = ZookeeperLockConfig.PREFIX)
public class ZookeeperLockConfig {

    public static final String PREFIX = "spring.zookeeper.lock";
    private String password;
    private String clusterServer;
    //lock
    private long waitTime = 60;
    private long leaseTime = 60;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public long getLeaseTime() {
        return leaseTime;
    }

    public void setLeaseTime(long leaseTime) {
        this.leaseTime = leaseTime;
    }

    public String getClusterServer() {
        return clusterServer;
    }

    public void setClusterServer(String clusterServer) {
        this.clusterServer = clusterServer;
    }
}