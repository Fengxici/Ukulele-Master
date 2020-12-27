package timing.ukulele.web.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import timing.ukulele.web.WebConstant;

import java.io.UnsupportedEncodingException;
import java.util.Date;


/**
 * @desc JWT工具类
 **/
@Slf4j
public final class JwtUtil {

    /**
     * 校验token是否正确
     *
     * @param token 令牌
     * @return 是否正确
     */
    public static boolean verify(String token) {
        try {
            //根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(WebConstant.TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            //效验TOKEN
            verifier.verify(token);
            return true;
        } catch (TokenExpiredException e) {
            log.info("token过期，token：{}", token);
        } catch (Exception e) {
            log.info("token无效，token：{}", token);
        }
        return false;
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(WebConstant.USERNAME).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    /**
     * 从cookie中获取
     *
     * @return
     */
    public static String getUsername() {
        String token = CookieUtil.get(WebConstant.TOKEN_COOKIE);
        return getUsername(token);
    }

    /**
     * 生成token
     *
     * @param username
     * @return token
     */
    public static String generateToken(String username) {
        Date date = new Date(System.currentTimeMillis() + WebConstant.EXPIRE_MILLIS);

        Algorithm algorithm = Algorithm.HMAC256(WebConstant.TOKEN_SECRET);
        // 附带username信息
        return JWT.create()
                .withClaim(WebConstant.USERNAME, username)
                .withExpiresAt(date)
                .sign(algorithm);
    }

}