package timing.ukulele.redisson.lock.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = RedisLockConfig.PREFIX)
public class RedisLockConfig {

    static final String PREFIX = "ukulele.redis.lock";
    //lock
    private long waitTime = 60;
    private long leaseTime = 60;

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

}
