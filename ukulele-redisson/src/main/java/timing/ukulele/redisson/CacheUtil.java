package timing.ukulele.redisson;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import timing.ukulele.redisson.cache.CacheManager;

@Configuration
public class CacheUtil {
    private static CacheManager cacheManager;

    @Bean
    public CacheManager setCache() {
        cacheManager = getCache();
        return cacheManager;
    }

    public static CacheManager getCache() {
        if (cacheManager == null) {
            synchronized (CacheUtil.class) {
                if (cacheManager == null) {
                    cacheManager = new RedissonHelper();
                }
            }
        }
        return cacheManager;
    }
}
