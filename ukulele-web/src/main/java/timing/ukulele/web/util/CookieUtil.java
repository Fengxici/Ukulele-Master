package timing.ukulele.web.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * cookie工具
 */
public final class CookieUtil extends WebIdentityUtil {

    /**
     * 向response中添加cookie
     *
     * @param name   cookie名
     * @param value  cookie值
     * @param maxAge 最大生存时间
     */
    public static void add(String name, String value, int maxAge) {
        HttpServletResponse response = getResponse();
        if (null != response) {
            Cookie cookie = new Cookie(name, value);
            cookie.setPath("/");
            cookie.setMaxAge(maxAge);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        }
    }

    /**
     * 获取cookie
     *
     * @param name cookie名
     * @return cookie
     */
    public static String get(String name) {
        HttpServletRequest request = getRequest();
        if (request == null)
            return null;
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        Cookie currentCookie = Arrays.stream(cookies)
                .filter(cookie -> name.equals(cookie.getName()))
                .findAny().orElse(null);
        if (currentCookie == null) {
            return null;
        }
        return currentCookie.getValue();
    }

    /**
     * 删除cookie
     *
     * @param name cookie名
     */
    public static void remove(String name) {
        HttpServletResponse response = getResponse();
        if (null != response) {
            Cookie cookie = new Cookie(name, "");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }
}
