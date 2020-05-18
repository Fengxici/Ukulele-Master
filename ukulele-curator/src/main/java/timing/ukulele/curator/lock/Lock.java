package timing.ukulele.curator.lock;

/**
 * @author fengxici
 */
public interface Lock {

    /**
     * 获取锁
     */
    boolean acquire();

    /**
     * 释放锁
     */
    boolean release();
}
