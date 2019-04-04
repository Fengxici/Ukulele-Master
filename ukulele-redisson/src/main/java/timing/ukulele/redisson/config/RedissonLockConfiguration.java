package timing.ukulele.redisson.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import timing.ukulele.redisson.core.BusinessKeyProvider;
import timing.ukulele.redisson.core.LockInfoProvider;
import timing.ukulele.redisson.core.RedissonLockAspectHandler;
import timing.ukulele.redisson.lock.LockFactory;

/**
 * Content :适用于内部低版本spring mvc项目配置,redisson外化配置
 */
@Configuration
@Import({RedissonLockAspectHandler.class})
public class RedissonLockConfiguration {
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
    @Bean
    public RedissonLockConfig redissonLockConfig(){
        return new RedissonLockConfig();
    }
}
