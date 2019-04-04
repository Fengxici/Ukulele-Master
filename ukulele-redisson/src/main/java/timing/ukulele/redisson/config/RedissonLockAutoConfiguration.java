package timing.ukulele.redisson.config;

import io.netty.channel.nio.NioEventLoopGroup;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.ClassUtils;
import timing.ukulele.redisson.core.BusinessKeyProvider;
import timing.ukulele.redisson.core.LockInfoProvider;
import timing.ukulele.redisson.core.RedissonLockAspectHandler;
import timing.ukulele.redisson.lock.LockFactory;

/**
 * Content :自动装配
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(RedissonLockConfig.class)
@Import({RedissonLockAspectHandler.class})
public class RedissonLockAutoConfiguration {

    @Autowired
    private RedissonLockConfig redissonLockConfig;

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    RedissonClient redisson() throws Exception {
        Config config = new Config();
        if(redissonLockConfig.getClusterServer()!=null){
            config.useClusterServers().setPassword(redissonLockConfig.getPassword())
                    .addNodeAddress(redissonLockConfig.getClusterServer().getNodeAddresses());
        }else {
            config.useSingleServer().setAddress(redissonLockConfig.getAddress())
                    .setDatabase(redissonLockConfig.getDatabase())
                    .setPassword(redissonLockConfig.getPassword());
        }
        Codec codec=(Codec) ClassUtils.forName(redissonLockConfig.getCodec(), ClassUtils.getDefaultClassLoader()).newInstance();
        config.setCodec(codec);
        config.setEventLoopGroup(new NioEventLoopGroup());
        return Redisson.create(config);
    }

    @Bean
    public LockInfoProvider lockInfoProvider(){
        return new LockInfoProvider();
    }

    @Bean
    public BusinessKeyProvider businessKeyProvider(){
        return new BusinessKeyProvider();
    }

    @Bean
    public LockFactory lockFactory(){
        return new LockFactory();
    }
}
