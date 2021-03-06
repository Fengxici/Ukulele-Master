package timing.ukulele.web.util;

import timing.ukulele.common.util.TypeParseUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author fengxici
 */
public final class Request2ListUtil {
    private Request2ListUtil() {
    }

    private static Integer paramSize(Set<Method> methodSet, Map<String, String[]> stringMap) {
        int size = 0;
        for (Method method : methodSet) {
            String key = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
            int tempSize = 0;
            if (stringMap.containsKey(key)) {
                tempSize = stringMap.get(key).length;
            }
            if (tempSize > size) {
                size = tempSize;
            }
        }
        return size;
    }

    public static <K> List<K> covert(Class<K> T, HttpServletRequest request) {
        try {

            List<K> objectList = new LinkedList<>();
            // 获取类的方法集合
            Set<Method> methodSet = getDeclaredMethods(T);
            Map<String, String[]> stringMap = request.getParameterMap();
            Integer valueSize = paramSize(methodSet, stringMap);
            System.out.println(T.getName() + " Max Length:" + valueSize);
            for (int i = 0; i < valueSize; i++) {
                K object = T.getDeclaredConstructor().newInstance();
                for (Method method : methodSet) {
                    String key = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
                    String[] value = stringMap.get(key);
                    if (value != null && i < value.length) {
                        Class<?>[] type = method.getParameterTypes();
                        Object[] paramValue = new Object[]{TypeParseUtil.convert(value[i], type[0], null)};
                        method.invoke(object, paramValue);
                    }
                }
                objectList.add(object);
            }
            return objectList;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 取自定义Set方法
     */
    private static <T> Set<Method> getDeclaredMethods(Class<T> T) {
        Method[] methods = T.getDeclaredMethods();
        Set<Method> methodSet = new HashSet<>();
        for (Method method : methods) {
            if (method.getName().startsWith("set")) {
                methodSet.add(method);
            }
        }
        return methodSet;
    }
}
