package timing.ukulele.common.constant;

/**
 * 第三方平台类型
 */
public enum ThirdPartyTypeEnum {
    /**
     * 微信小程序
     */
    WX_APP(1, "微信小程序");

    // 第三方平台类型
    private Integer value;

    // 第三放平台描述
    private String description;

    ThirdPartyTypeEnum(Integer value, String description) {
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
