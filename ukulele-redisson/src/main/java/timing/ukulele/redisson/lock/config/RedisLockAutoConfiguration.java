package timing.ukulele.redisson.lock.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import timing.ukulele.redisson.TimingRedisAutoConfiguration;
import timing.ukulele.redisson.lock.LockFactory;
import timing.ukulele.redisson.lock.core.BusinessKeyProvider;
import timing.ukulele.redisson.lock.core.LockInfoProvider;
import timing.ukulele.redisson.lock.core.RedisLockAspectHandler;

/**
 * Content :自动装配
 * @author fengxici
 */
@Configuration
@AutoConfigureAfter(TimingRedisAutoConfiguration.class)
@EnableConfigurationProperties(RedisLockConfig.class)
@Import({RedisLockAspectHandler.class})
public class RedisLockAutoConfiguration {

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
