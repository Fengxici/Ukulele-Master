package timing.ukulele.redisson.cache;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import timing.ukulele.redisson.TimingRedisAutoConfiguration;

@Configuration
@AutoConfigureAfter(TimingRedisAutoConfiguration.class)
public class CacheAutoConfiguration {
    private static CacheManager cacheManager;

    @Bean
    public CacheManager setCache() {
        cacheManager = getCache();
        return cacheManager;
    }

    public static CacheManager getCache() {
        if (cacheManager == null) {
            synchronized (CacheAutoConfiguration.class) {
                if (cacheManager == null) {
                    cacheManager = new RedisHelper();
                }
            }
        }
        return cacheManager;
    }
}
