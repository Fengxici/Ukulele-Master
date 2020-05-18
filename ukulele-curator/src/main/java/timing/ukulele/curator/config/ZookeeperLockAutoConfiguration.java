package timing.ukulele.curator.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import timing.ukulele.curator.core.BusinessKeyProvider;
import timing.ukulele.curator.core.LockInfoProvider;
import timing.ukulele.curator.core.ZookeeperLockAspectHandler;
import timing.ukulele.curator.lock.LockFactory;

/**
 * Content :自动装配
 *
 * @author fengxici
 */
@Configuration
@EnableConfigurationProperties(ZookeeperLockConfig.class)
@Import({ZookeeperLockAspectHandler.class})
public class ZookeeperLockAutoConfiguration {

    private final ZookeeperLockConfig zookeeperLockConfig;

    public ZookeeperLockAutoConfiguration(ZookeeperLockConfig zookeeperLockConfig) {
        this.zookeeperLockConfig = zookeeperLockConfig;
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean
    CuratorFramework curatorClient() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);

        // 实例化Curator客户端
        // 使用工厂类来建造客户端的实例对象
        CuratorFramework client = CuratorFrameworkFactory.builder()
                // 放入zookeeper服务器ip
                .connectString(zookeeperLockConfig.getClusterServer())
                // 设4定会话时间以及重连策略
                .sessionTimeoutMs(10000).retryPolicy(retryPolicy)
                // 设置命名空间以及开始建立连接
                .namespace("zookeeper-lock").build();
        client.start();
        return client;
    }

    @Bean
    public LockInfoProvider lockInfoProvider() {
        return new LockInfoProvider();
    }

    @Bean
    public BusinessKeyProvider businessKeyProvider() {
        return new BusinessKeyProvider();
    }

    @Bean
    public LockFactory lockFactory() {
        return new LockFactory();
    }
}

