package timing.ukulele.web.util;


import lombok.extern.slf4j.Slf4j;
import timing.ukulele.common.util.TypeParseUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public final class Request2ModelUtil {

    private Request2ModelUtil() {
    }

    public static <K> K covert(Class<K> T, HttpServletRequest request) {
        try {
            K obj = T.getDeclaredConstructor().newInstance();
            // 获取类的方法集合
            Set<Method> methodSet = getMethods(T);
            for (Method method : methodSet) {
                String key = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
                String value = request.getParameter(key);
                Class<?>[] type = method.getParameterTypes();
                Object[] paramValue = new Object[]{TypeParseUtil.convert(value, type[0], null)};
                method.invoke(obj, paramValue);
            }
            return obj;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 取全部Set方法
     *
     * @param T calss
     * @return method set
     */
    public static Set<Method> getMethods(Class<?> T) {
        Method[] methods = T.getMethods();
        Set<Method> methodSet = new HashSet<>();
        for (Method method : methods) {
            if (method.getName().startsWith("set")) {
                methodSet.add(method);
            }
        }
        return methodSet;
    }

    /**
     * 取自定义Set方法
     *
     * @param T calss
     * @return method set
     */
    public static Set<Method> getDeclaredMethods(Class<?> T) {
        Method[] methods = T.getMethods();
        Set<Method> methodSet = new HashSet<>();
        for (Method method : methods) {
            if (method.getName().startsWith("set")) {
                methodSet.add(method);
            }
        }
        return methodSet;
    }

    /**
     * 取自定义get方法
     *
     * @param T class
     * @return declared methods set
     */
    public static Set<Method> getGetDeclaredMethods(Class<?> T) {
        Method[] methods = T.getDeclaredMethods();
        Set<Method> methodSet = new HashSet<>();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                methodSet.add(method);
            }
        }
        return methodSet;
    }

    /**
     * 根据传递的参数修改数据
     *
     * @param o            object
     * @param parameterMap parameter map
     */
    public static void covertObj(Object o, Map<String, String[]> parameterMap) {
        Class<?> clazz = o.getClass();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey().trim();
            String value = entry.getValue()[0].trim();
            try {
                Method method = setMethod(key, clazz);
                if (method != null) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Object[] paramValue = new Object[]{TypeParseUtil.convert(value, parameterTypes[0], null)};
                    method.invoke(o, paramValue);
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    /**
     * 根据传递的参数修改数据
     *
     * @param o            object
     * @param parameterMap map参数
     */
    public static void covertObjWithMap(Object o, Map<String, String> parameterMap) {
        Class<?> clazz = o.getClass();
        for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
            String key = entry.getKey().trim();
            String value = entry.getValue().trim();
            try {
                Method method = setMethod(key, clazz);
                if (method != null) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Object[] paramValue = new Object[]{TypeParseUtil.convert(value, parameterTypes[0], null)};
                    method.invoke(o, paramValue);
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    /**
     * 根据传递的参数修改数据
     *
     * @param o        object
     * @param paramObj model参数
     */
    public static void covertObj(Object o, Object paramObj) {
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field item : fields) {
            try {
                Method getMethod = getMethod(item.getName(), paramObj.getClass());
                if (getMethod != null) {
                    Object value = getMethod.invoke(paramObj);
                    Method setMethod = setMethod(item.getName(), o.getClass());
                    if (setMethod != null) {
                        if (value != null && !"".equals(value.toString())) {
                            setMethod.invoke(o, value);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    /**
     * @param obj       obj
     * @param obiExtend objExtend
     * @return object
     */
    public static Object init(Object obj, Object obiExtend) {
        Class<?> clazz = obj.getClass();
        Set<Method> getMethods = Request2ModelUtil.getGetDeclaredMethods(clazz);
        for (Method method : getMethods) {
            try {
                String name = method.getName();
                String fileName = name.substring(3, 4).toLowerCase() + name.substring(4, name.length());
                Object o = method.invoke(obj);
                Method setMethod = Request2ModelUtil.setMethod(fileName, clazz);
                assert setMethod != null;
                setMethod.invoke(obiExtend, o);
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return obiExtend;
    }

    public static Method setMethod(String fieldName, Class<?> clazz) {
        try {
            Class<?>[] parameterTypes = new Class[1];
            Field field = clazz.getDeclaredField(fieldName);
            parameterTypes[0] = field.getType();
            String sb = "set" +
                    fieldName.substring(0, 1).toUpperCase() +
                    fieldName.substring(1);
            return clazz.getMethod(sb, parameterTypes);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public static Method getMethod(String fieldName, Class<?> clazz) {
        StringBuilder sb = new StringBuilder();
        sb.append("get");
        sb.append(fieldName.substring(0, 1).toUpperCase());
        sb.append(fieldName.substring(1));
        try {
            return clazz.getMethod(sb.toString());
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }
}
