package timing.ukulele.web.interceptor;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import timing.ukulele.common.util.JsonUtils;
import timing.ukulele.web.annotation.RequiredPermission;
import timing.ukulele.web.service.PermissionService;
import timing.ukulele.web.util.Request2ModelUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * •角色权限拦截器
 * •@className: PermissionInterceptor
 * •@author: 吕自聪
 * •@date: 2019/9/29
 */
public class PermissionInterceptor implements HandlerInterceptor {
    private final PermissionService permissionService;

    @Autowired
    public PermissionInterceptor(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (null == permissionService)
            throw new RuntimeException("尚未设置角色权限服务");
        // 验证权限
        if (this.hasPermission(request, handler)) {
            return true;
        }
        // 如果没有权限 则抛403异常 springboot会处理，跳转到 /error/403 页面
        response.sendError(HttpStatus.FORBIDDEN.value(), "无权限");
        return false;
    }

    /**
     * 是否有权限
     *
     * @param handler
     * @return
     */
    private boolean hasPermission(HttpServletRequest request, Object handler) {
        String acl = request.getHeader("x-role-header");
        if (StringUtils.isEmpty(acl))
            return false;
        String[] aclArray = acl.split(",");
        Set<String> aclSet = new HashSet<>(aclArray.length);
        for (String s : aclArray) {
            aclSet.add(s.trim());
        }
        //没有设置角色则不通过
        if (CollectionUtils.isEmpty(aclSet))
            return false;
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取方法上的角色与权限注解
            RequiredPermission requiredPermission = handlerMethod.getMethod().getAnnotation(RequiredPermission.class);
            //如果为空则表示不需要权限与角色，放行
            if (requiredPermission == null) {
                return true;
            }
            if (StringUtils.isEmpty(requiredPermission.ability()) || requiredPermission.acl().length < 1 || StringUtils.isEmpty(requiredPermission.router()))
                throw new RuntimeException("角色权限未设置");
            Set<String> abilitySet = permissionService.abilitySet(requiredPermission.router(), Arrays.asList(requiredPermission.acl()));
            //没有设置权限点则不通过
            if (CollectionUtils.isEmpty(abilitySet))
                return false;
            //没有权限点则不通过
            if (!abilitySet.contains(requiredPermission.ability()))
                return false;
            Set<String> requiredAclSet = new HashSet<>(Arrays.asList(requiredPermission.acl()));
            Set<String> result = new HashSet<>(aclSet);
            //去交集
            result.retainAll(requiredAclSet);
            //要求所有权限则返回交集大小是否等于需要的权限集合大小
            if (requiredPermission.aclAll())
                return result.size() == requiredAclSet.size();
            else
                return result.size() > 0;
        }
        return true;
    }

}