package timing.ukulele.curator.lock;

public interface Lock {

    boolean acquire();

    boolean release();
}
