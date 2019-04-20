package timing.ukulele.common.data;

public enum ResponseCode {

    SUCCESS(1000, "成功."),
    ERROR(1001, "失败."),
    PARA_ERROR(1002, "参数错误."),
    FACADE_ERROR(2000, "接口调用失败"),
    BUSINESS_ERROR(3000, "业务处理失败");

    ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;

    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
