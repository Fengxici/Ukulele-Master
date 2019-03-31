package timing.ukulele.oauth.config.annotation;

import org.springframework.context.annotation.Import;
import timing.ukulele.oauth.config.store.DBTokenStore;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 在启动类上添加该注解来----开启通过数据库存储令牌
 *               数据库 schema ：https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/test/resources/schema.sql
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DBTokenStore.class)
public @interface EnableDBTokenStore {
}
