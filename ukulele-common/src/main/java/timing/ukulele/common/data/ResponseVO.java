package timing.ukulele.common.data;

import timing.ukulele.common.exception.IError;

import java.io.Serializable;

/**
 * 视图层返回结果
 */
public class ResponseVO<T> implements Serializable {

    private String errorCode;
    private String errorMessage;
    private String extMessage;
    private T result;
    private ResponseVO.Status status;

    public ResponseVO.Status getStatus() {
        return this.status;
    }

    public void setStatus(ResponseVO.Status status) {
        this.status = status;
    }

    public ResponseVO() {
        this.status = ResponseVO.Status.SUCCEED;
    }

    public ResponseVO(IError error) {
        this.errorCode = error.getErrorCode();
        this.errorMessage = error.getErrorMessage();
        this.status = ResponseVO.Status.FAILED;
    }

    public static ResponseVO<Object> success() {
        return new ResponseVO<>();
    }

    public static ResponseVO<Object> success(Object result) {
        ResponseVO<Object> response = new ResponseVO<>();
        response.setResult(result);
        return response;
    }

    public static ResponseVO<Object> failure(IError error) {
        ResponseVO<Object> response = new ResponseVO<>();
        response.errorCode = error.getErrorCode();
        response.errorMessage = error.getErrorMessage();
        response.status = Status.FAILED;
        return response;
    }

    public static ResponseVO<Object> failure(String message) {
        ResponseVO<Object> response = new ResponseVO<>();
        response.setErrorMessage(message);
        response.status = Status.FAILED;
        return response;
    }

    public static ResponseVO<Object> warring(Object result) {
        ResponseVO<Object> response = new ResponseVO<>();
        response.setResult(result);
        response.status = Status.WARRING;
        return response;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getExtMessage() {
        return this.extMessage;
    }

    public void setExtMessage(String extMessage) {
        this.extMessage = extMessage;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.errorCode != null) {
            sb.append("ErrorCode : ").append(this.errorCode).append("ErrorMessage : ").append(this.errorMessage).append("ExtMessage : " + this.extMessage);
        } else {
            sb.append("Succeed");
        }

        return sb.toString();
    }

    public enum Status {
        /**
         * 成功
         */
        SUCCEED,
        /**
         * 警告
         */
        WARRING,
        /**
         * 失败
         */
        FAILED;

        Status() {
        }
    }
}
