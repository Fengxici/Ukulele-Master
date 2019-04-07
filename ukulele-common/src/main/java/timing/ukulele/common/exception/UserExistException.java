package timing.ukulele.common.exception;


/**
 * 用户已存在
 */
public class UserExistException extends BusinessException {

    public UserExistException(String message) {
        super(message);
    }

}
