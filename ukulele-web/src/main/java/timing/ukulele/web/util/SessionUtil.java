package timing.ukulele.web.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * session工具
 */
public final class SessionUtil extends WebIdentityUtil {
    // 获取一个session对象
    private static HttpSession session = getSession();

    /**
     * 获取session 的方法
     *
     * @return HttpSession
     */
    public static HttpSession getSession() {
        HttpSession session = null;
        try {
            HttpServletRequest request = getRequest();
            if (null != request)
                session = request.getSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return session;
    }

    /**
     * 设置一个session值
     *
     * @param name  键
     * @param value 值
     * @param <T>   值类型
     */
    public static <T> void set(String name, T value) {
        session.setAttribute(name, value);
    }

    /**
     * 获取一个session值
     *
     * @param name 键
     * @param <T>  值类型
     * @return session值
     */
    public static <T> T get(String name) {
        if (null == session)
            return null;
        Object value = session.getAttribute(name);
        if (null == value)
            return null;
        return (T) value;
    }

    /**
     * 移除一个session
     *
     * @param name 键
     */
    public static void remove(String name) {
        session.removeAttribute(name);
    }

}
