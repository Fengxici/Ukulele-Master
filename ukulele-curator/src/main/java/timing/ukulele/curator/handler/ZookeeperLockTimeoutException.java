package timing.ukulele.curator.handler;

/**
 * @author fengxici
 */
public class ZookeeperLockTimeoutException extends RuntimeException {

    public ZookeeperLockTimeoutException() {
    }

    public ZookeeperLockTimeoutException(String message) {
        super(message);
    }

    public ZookeeperLockTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
