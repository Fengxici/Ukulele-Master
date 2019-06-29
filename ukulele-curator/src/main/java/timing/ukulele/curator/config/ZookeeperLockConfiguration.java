package timing.ukulele.curator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import timing.ukulele.curator.core.BusinessKeyProvider;
import timing.ukulele.curator.core.LockInfoProvider;
import timing.ukulele.curator.core.ZookeeperLockAspectHandler;
import timing.ukulele.curator.lock.LockFactory;

/**
 * Content :适用于内部低版本spring mvc项目配置,redisson外化配置
 */
@Configuration
@Import({ZookeeperLockAspectHandler.class})
public class ZookeeperLockConfiguration {
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
    public ZookeeperLockConfig zookeeperLockConfig(){
        return new ZookeeperLockConfig();
    }
}

