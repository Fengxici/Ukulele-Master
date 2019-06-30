package timing.ukulele.redisson.lock.handler;

public class RedisLockInvocationException extends RuntimeException {

    public RedisLockInvocationException() {
    }

    public RedisLockInvocationException(String message) {
        super(message);
    }

    public RedisLockInvocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
