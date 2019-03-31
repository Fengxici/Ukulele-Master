package timing.ukulele.common.exception;

/**
 * 实例异常
 */
@SuppressWarnings("serial")
public class InstanceException extends RuntimeException {
    /**
     * 无参数构造方法
     */
    public InstanceException() {
        super();
    }

    /**
     * 构造一个具有指定原因和（cause == null？null：cause.toString（））（通常包含原因的类和详细消息）的详细消息的新throwable。
     *
     * @param cause 原因。 （空值是允许的，并指出原因不存在或未知。）
     */
    public InstanceException(Throwable cause) {
        super(cause);
    }
}
