package timing.ukulele.eureka;


import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fengxici
 */
@Configuration
public class EurekaClientServiceRegistryAutoConfiguration {

    @Bean
    @ConditionalOnBean(EurekaRegistration.class)
    public ServiceRegistryController serviceRegistryController() {
        return new ServiceRegistryController();
    }
}