package timing.ukulele.curator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = ZookeeperLockConfig.PREFIX)
public class ZookeeperLockConfig {

    public static final String PREFIX = "timing.lock.zookeeper";
    private String password;
    private String clusterServer;
    //lock
    private long waitTime = 60;

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

    public String getClusterServer() {
        return clusterServer;
    }

    public void setClusterServer(String clusterServer) {
        this.clusterServer = clusterServer;
    }
}
