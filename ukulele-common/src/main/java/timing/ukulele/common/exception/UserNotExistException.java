package timing.ukulele.common.exception;


/**
 * 用户未存在
 */
public class UserNotExistException extends BusinessException {

    public UserNotExistException(String message) {
        super(message);
    }

}
