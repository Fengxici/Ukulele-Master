package timing.ukulele.curator.annotation;

import timing.ukulele.curator.model.LockTimeoutStrategy;
import timing.ukulele.curator.model.LockType;
import timing.ukulele.curator.model.ReleaseTimeoutStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Content :加锁注解
 *
 * @author fengxici
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ZookeeperLock {
    /**
     * 锁的名称
     */
    String name() default "";

    /**
     * 锁类型，默认可重入锁
     */
    LockType lockType() default LockType.Reentrant;

    /**
     * 尝试加锁，最多等待时间
     */
    long waitTime() default Long.MIN_VALUE;

    /**
     * 自定义业务key
     */
    String[] keys() default {};

    /**
     * 加锁超时的处理策略
     */
    LockTimeoutStrategy lockTimeoutStrategy() default LockTimeoutStrategy.NO_OPERATION;

    /**
     * 自定义加锁超时的处理策略
     */
    String customLockTimeoutStrategy() default "";

    /**
     * 释放锁时已超时的处理策略
     */
    ReleaseTimeoutStrategy releaseTimeoutStrategy() default ReleaseTimeoutStrategy.NO_OPERATION;

    /**
     * 自定义释放锁时已超时的处理策略
     */
    String customReleaseTimeoutStrategy() default "";

}

