package timing.ukulele.web.wrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Map;

/**
 * 请求参数包装类
 */
public class ParameterRequestWrapper extends HttpServletRequestWrapper {
    private Map<String, String[]> map;

    public ParameterRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public ParameterRequestWrapper(HttpServletRequest request, Map<String, String[]> map) {
        super(request);
        this.map = map;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return this.map;
    }

    @Override
    public String[] getParameterValues(String name) {
        if (map != null) {
            return map.get(name);
        }
        return super.getParameterValues(name);
    }
}
