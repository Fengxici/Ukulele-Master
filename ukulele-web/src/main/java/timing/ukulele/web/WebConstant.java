package timing.ukulele.web;

import java.util.concurrent.TimeUnit;

public interface WebConstant {
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