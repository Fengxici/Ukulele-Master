package timing.ukulele.curator.handler;

/**
 * @author fengxici
 */
public class ZookeeperLockInvocationException extends RuntimeException {

    public ZookeeperLockInvocationException() {
    }

    public ZookeeperLockInvocationException(String message) {
        super(message);
    }

    public ZookeeperLockInvocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
