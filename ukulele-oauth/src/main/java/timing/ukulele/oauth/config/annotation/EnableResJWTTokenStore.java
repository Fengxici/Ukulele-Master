package timing.ukulele.oauth.config.annotation;

import org.springframework.context.annotation.Import;
import timing.ukulele.oauth.config.store.ResourceJWTTokenStore;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 在启动类上添加该注解来----开启 JWT 令牌存储（资源服务器-非对称加密）
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ResourceJWTTokenStore.class)
public @interface EnableResJWTTokenStore {
}
