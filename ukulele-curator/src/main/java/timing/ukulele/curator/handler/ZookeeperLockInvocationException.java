package timing.ukulele.curator.handler;

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
