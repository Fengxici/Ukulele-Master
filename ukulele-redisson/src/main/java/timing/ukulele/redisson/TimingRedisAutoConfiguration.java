package timing.ukulele.redisson;

import io.netty.channel.nio.NioEventLoopGroup;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import timing.ukulele.redisson.properties.RedisProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@ConditionalOnClass({Redisson.class})
@ConditionalOnExpression("'${timing.redis.mode}'=='single' or '${timing.redis.mode}'=='cluster' or '${timing.redis.mode}'=='sentinel'")
public class TimingRedisAutoConfiguration {
    private final RedisProperties redisProperties;

    @Autowired
    public TimingRedisAutoConfiguration(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    /**
     * 单机模式 redisson 客户端
     */
    @Bean(destroyMethod = "shutdown")
    @ConditionalOnProperty(name = "timing.redis.mode", havingValue = "single")
    RedissonClient redissonSingle() throws Exception {
        Config config = new Config();
        String node = redisProperties.getSingle().getAddress();
        node = node.startsWith("redis://") ? node : "redis://" + node;
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(node).setDatabase(redisProperties.getDatabase())
                .setTimeout(redisProperties.getPool().getConnTimeout())
                .setConnectionPoolSize(redisProperties.getPool().getSize())
                .setConnectionMinimumIdleSize(redisProperties.getPool().getMinIdle());
        if (!StringUtils.isEmpty(redisProperties.getPassword())) {
            serverConfig.setPassword(redisProperties.getPassword());
        }
        setCodec(config);
        return Redisson.create(config);
    }

    /**
     * 集群模式的 redisson 客户端
     */
    @Bean(destroyMethod = "shutdown")
    @ConditionalOnProperty(name = "timing.redis.mode", havingValue = "cluster")
    RedissonClient redissonCluster() throws Exception {
        System.out.println("cluster redisProperties:" + redisProperties.getCluster());
        Config config = new Config();
        String[] nodes = redisProperties.getCluster().getNodes().split(",");
        List<String> newNodes = new ArrayList<>(nodes.length);
        Arrays.stream(nodes).forEach((index) -> newNodes.add(
                index.startsWith("redis://") ? index : "redis://" + index));

        ClusterServersConfig serverConfig = config.useClusterServers()
                .addNodeAddress(newNodes.toArray(new String[0]))
                .setScanInterval(
                        redisProperties.getCluster().getScanInterval())
                .setIdleConnectionTimeout(
                        redisProperties.getPool().getSoTimeout())
                .setConnectTimeout(
                        redisProperties.getPool().getConnTimeout())
                .setFailedAttempts(
                        redisProperties.getCluster().getFailedAttempts())
                .setRetryAttempts(
                        redisProperties.getCluster().getRetryAttempts())
                .setRetryInterval(
                        redisProperties.getCluster().getRetryInterval())
                .setMasterConnectionPoolSize(redisProperties.getCluster()
                        .getMasterConnectionPoolSize())
                .setSlaveConnectionPoolSize(redisProperties.getCluster()
                        .getSlaveConnectionPoolSize())
                .setTimeout(redisProperties.getTimeout());
        if (!StringUtils.isEmpty(redisProperties.getPassword())) {
            serverConfig.setPassword(redisProperties.getPassword());
        }
        setCodec(config);
        return Redisson.create(config);
    }

    /**
     * 哨兵模式 redisson 客户端
     */
    @Bean(destroyMethod = "shutdown")
    @ConditionalOnProperty(name = "timing.redis.mode", havingValue = "sentinel")
    RedissonClient redissonSentinel() throws Exception {
        System.out.println("sentinel redisProperties:" + redisProperties.getSentinel());
        Config config = new Config();
        String[] nodes = redisProperties.getSentinel().getNodes().split(",");
        List<String> newNodes = new ArrayList<>(nodes.length);
        Arrays.stream(nodes).forEach((index) -> newNodes.add(
                index.startsWith("redis://") ? index : "redis://" + index));
        SentinelServersConfig serverConfig = config.useSentinelServers()
                .addSentinelAddress(newNodes.toArray(new String[0]))
                .setMasterName(redisProperties.getSentinel().getMaster())
                .setReadMode(ReadMode.SLAVE)
                .setFailedAttempts(redisProperties.getSentinel().getFailMax())
                .setTimeout(redisProperties.getTimeout())
                .setMasterConnectionPoolSize(redisProperties.getPool().getSize())
                .setSlaveConnectionPoolSize(redisProperties.getPool().getSize());
        if (!StringUtils.isEmpty(redisProperties.getPassword())) {
            serverConfig.setPassword(redisProperties.getPassword());
        }
        setCodec(config);
        return Redisson.create(config);
    }

    /**
     * 设置codec
     */
    private void setCodec(Config config) throws Exception {
        Codec codec = (Codec) ClassUtils.forName(redisProperties.getCodec(), ClassUtils.getDefaultClassLoader()).newInstance();
        config.setCodec(codec);
        config.setEventLoopGroup(new NioEventLoopGroup());
    }
}

