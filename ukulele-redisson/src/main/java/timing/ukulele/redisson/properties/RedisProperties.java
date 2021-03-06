package timing.ukulele.redisson.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author fengxici
 */
@ConfigurationProperties(prefix = "ukulele.redis")
public class RedisProperties {
    private int database;

    /**
     * 等待节点回复命令的时间。该时间从命令发送成功时开始计时
     */
    private int timeout;

    private String password;

    private String mode;

    private String codec = "org.redisson.codec.JsonJacksonCodec";

    /**
     * 池配置
     */
    private RedisPoolProperties pool;

    /**
     * 单机信息配置
     */
    private RedisSingleProperties single;

    /**
     * 集群 信息配置
     */
    private RedisClusterProperties cluster;

    /**
     * 哨兵配置
     */
    private RedisSentinelProperties sentinel;

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public RedisPoolProperties getPool() {
        return pool;
    }

    public void setPool(RedisPoolProperties pool) {
        this.pool = pool;
    }

    public RedisSingleProperties getSingle() {
        return single;
    }

    public void setSingle(RedisSingleProperties single) {
        this.single = single;
    }

    public RedisClusterProperties getCluster() {
        return cluster;
    }

    public void setCluster(RedisClusterProperties cluster) {
        this.cluster = cluster;
    }

    public RedisSentinelProperties getSentinel() {
        return sentinel;
    }

    public void setSentinel(RedisSentinelProperties sentinel) {
        this.sentinel = sentinel;
    }
}
