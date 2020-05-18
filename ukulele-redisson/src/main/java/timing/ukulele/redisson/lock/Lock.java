package timing.ukulele.redisson.lock;

/**
 * @author fengxici
 */
public interface Lock {

    boolean acquire();

    boolean release();
}
