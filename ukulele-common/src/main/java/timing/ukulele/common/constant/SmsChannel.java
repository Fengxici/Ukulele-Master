package timing.ukulele.common.constant;

/**
 * 短信通道
 */
public enum SmsChannel {

    /**
     * 阿里云短信
     */
    ALIYUN("ALIYUN_SMS", "阿里云短信");

    /*** 通道名称*/
    private String name;

    /*** 通道描述*/
    private String description;

    SmsChannel(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
