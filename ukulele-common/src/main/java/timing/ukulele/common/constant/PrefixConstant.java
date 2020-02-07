package timing.ukulele.common.constant;

/**
 * 前缀常量
 */
public interface PrefixConstant {
    /**
     * 短信验证码前缀
     */
    String SMS_CODE = "SMS_CODE_";

    /**
     * 已发送的手机号，防止同一个手机号多次请求
     */
    String SMS_PHONE_SENDED = "SMS_PHONE_SENDED_";

    /**
     * 二维码连接标识
     */
    String QR_CONNECT_ID = "qr_connect_id_";
}
