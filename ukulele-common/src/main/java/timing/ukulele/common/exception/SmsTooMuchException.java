package timing.ukulele.common.exception;


/**
 * 短信发送太频繁
 */
public class SmsTooMuchException extends BusinessException {

    public SmsTooMuchException(String message) {
        super(message);
    }

}
