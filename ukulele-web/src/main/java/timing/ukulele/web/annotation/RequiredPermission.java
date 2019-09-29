package timing.ukulele.web.annotation;

import java.lang.annotation.*;

/**
 * •角色权限注解
 * •@className: RequiredPermission
 * •@author: 吕自聪
 * •@date: 2019/9/29
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequiredPermission {

    String router();

    String[] acl() default {};

    boolean aclAll() default false;

    String ability();
}
