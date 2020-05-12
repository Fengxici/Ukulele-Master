package timing.ukulele.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则验证工具
 */
public final class RegexUtil {
    private static Pattern regex = Pattern.compile("^1[345789]\\d{9}$");

    /**
     * 手机号验证
     *
     * @param mobileNumber
     * @return
     */
    public static boolean checkMobileNumber(String mobileNumber) {
        try {
            Matcher matcher = regex.matcher(mobileNumber);
            return matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
