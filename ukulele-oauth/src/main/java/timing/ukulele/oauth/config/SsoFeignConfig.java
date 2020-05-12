package timing.ukulele.oauth.config;

import feign.Contract;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.auth.BasicAuthRequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 使用说明：
 * 在@FeignClient注解中为configuration属性指定拦截器
 *
 * @FeignClient( ... configuration = SsoFeignConfig.class)
 */
@Component
@Slf4j
public class SsoFeignConfig implements RequestInterceptor {

    private static final String BEARER_TOKEN_TYPE = "bearer";
    private final static String TOKEN_HEADER = "Authorization";

    @Override
    public void apply(RequestTemplate template) {
        template.header(TOKEN_HEADER, getHeaders(getHttpServletRequest()));
    }

    private HttpServletRequest getHttpServletRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                return attributes.getRequest();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        return null;
    }

    private String getHeaders(HttpServletRequest request) {
        if (request != null) {
            Enumeration<String> enumeration = request.getHeaderNames();
            while (enumeration.hasMoreElements()) {
                String key = enumeration.nextElement();
                if (TOKEN_HEADER.equals(key)) {
                    return String.format("%s %s", BEARER_TOKEN_TYPE, request.getHeader(key));
                }
            }
        }
        return "";
    }

    @Bean
    public Contract feignContract() {
        return new feign.Contract.Default();
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
//        TODO 从配置文件中读取
        return new BasicAuthRequestInterceptor("root", "root");
    }
}
