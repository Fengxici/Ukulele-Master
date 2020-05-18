package timing.ukulele.redisson.lock.handler;

/**
 * @author fengxici
 */
public class RedisLockTimeoutException extends RuntimeException {

    public RedisLockTimeoutException() {
    }

    public RedisLockTimeoutException(String message) {
        super(message);
    }

    public RedisLockTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
