package timing.ukulele.curator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author fengxici
 */
@ConfigurationProperties(prefix = "ukulele.zookeeper.lock")
public class ZookeeperLockConfig {
    private String password;
    private String clusterServer;
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
