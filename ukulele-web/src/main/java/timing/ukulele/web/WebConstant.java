package timing.ukulele.web;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public interface WebConstant {
    DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    /**
     * 请求头角色常量
     */
    String X_ROLE_HEADER = "x-role-header";
    /**
     * 请求头用户常量
     */
    String X_USER_HEADER = "x-user-header";

    String TOKEN_COOKIE = "token";

    int EXPIRE_HOURS = 12;

    long EXPIRE_MILLIS = TimeUnit.HOURS.toMillis(EXPIRE_HOURS);

    String TOKEN_SECRET = "123";

    String USERNAME = "USERNAME";
}