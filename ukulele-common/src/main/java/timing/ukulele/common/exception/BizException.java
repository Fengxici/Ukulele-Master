package timing.ukulele.common.exception;

/**
 * 业务异常类
 */
public class BizException extends RuntimeException {
    /**
     * 无参数构造方法
     */
    public BizException() {
    }

    /**
     * 用指定的详细信息message构造一个新的throwable。
     *
     * @param message 详细消息。 详细消息被保存以供以后通过getMessage（）方法检索.
     */
    public BizException(String message) {
        super(message);
    }

    /**
     * 用指定的详细信息message和Throwable类型的cause构造一个新的throwable。
     *
     * @param message 详细消息（保存以供以后通过getMessage（）方法检索）。
     * @param cause   原因（保存以供以后通过getCause（）方法检索）。 （空值是允许的，并指出原因不存在或未知。）
     */
    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造一个具有指定原因和（cause == null？null：cause.toString（））（通常包含原因的类和详细消息）的详细消息的新throwable。
     *
     * @param cause 原因。 （空值是允许的，并指出原因不存在或未知。）
     */
    public BizException(Throwable cause) {
        super(cause);
    }

    /**
     * 四个参数的构造方法
     *
     * @param message            详细消息。 详细消息被保存以供以后通过getMessage（）方法检索。
     * @param cause              原因。 （空值是允许的，并指出原因不存在或未知。）
     * @param enableSuppression  是否启用或禁用抑制
     * @param writableStackTrace 堆栈跟踪是否可写
     */
    public BizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
