package timing.ukulele.oauth.config.annotation;

import org.springframework.context.annotation.Import;
import timing.ukulele.oauth.config.service.DBClientDetailsService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 在启动类上添加该注解来----开启从数据库加载客户端详情
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DBClientDetailsService.class)
public @interface EnableDBClientDetailsService {
}
