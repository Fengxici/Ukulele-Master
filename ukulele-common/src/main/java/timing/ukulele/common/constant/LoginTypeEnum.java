package timing.ukulele.common.constant;

/**
 * 登录类型
 */
public enum LoginTypeEnum {
    /**
     * 用户名密码
     */
    PASSWORD(1, "用户名密码"),
    PHONE(2, "手机号验证码"),
    QRCODE(3, "二维码"),
    THIRD_OPEN(4, "第三方开放平台"),
    THIRD_OAUTH(5, "第三方oauth平台");
    // 类型值
    private Integer value;

    // 类型描述
    private String description;

    LoginTypeEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
