package timing.ukulele.ribbon;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import static timing.ukulele.ribbon.RibbonConstant.HEADER_LABEL;
import static timing.ukulele.ribbon.RibbonConstant.HEADER_LABEL_SPLIT;

/**
 * Feign请求拦截器
 * 用于在Feign请求间传递label信息
 */
@Slf4j
public class XlabelFeignHeaderInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        // 在请求之前初始化 Hystrix Context 避免报错:
        // HystrixRequestContext.initializeContext() must be called at the beginning of each request before RequestVariable functionality can be used.
        if (!HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.initializeContext();
        }
        String header = StringUtils.collectionToDelimitedString(XlabelMvcHeaderInterceptor.LABEL.get(), HEADER_LABEL_SPLIT);
        log.debug("Pass x-label by feign: " + header);
        template.header(HEADER_LABEL, header);
    }
}
