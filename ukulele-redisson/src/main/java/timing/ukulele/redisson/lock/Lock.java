package timing.ukulele.redisson.lock;

public interface Lock {

    boolean acquire();

    boolean release();
}
