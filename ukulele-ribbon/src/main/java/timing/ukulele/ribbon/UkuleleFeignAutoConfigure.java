package timing.ukulele.ribbon;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * Feign统一配置
 */
@Configuration
public class UkuleleFeignAutoConfigure {

    /**
     * Feign 日志级别
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public XlabelFeignHeaderInterceptor xlabelFeignHeaderInterceptor() {
        return new XlabelFeignHeaderInterceptor();
    }
}
