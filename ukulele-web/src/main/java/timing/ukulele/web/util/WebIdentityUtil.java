package timing.ukulele.web.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class WebIdentityUtil {
    /**
     * 获取一个request对象的方法
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == attrs)
            return null;
        return attrs.getRequest();
    }

    /**
     * 获取一个response对象的方法
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == attrs)
            return null;
        return attrs.getResponse();
    }
}
