package edu.njnu.reproducibility.common.untils;



import edu.njnu.reproducibility.common.enums.ResultEnum;
import edu.njnu.reproducibility.common.exception.MyException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;

/**
 * @ClassName JwtUtils
 * @Description todo
 * @Author sun_liber
 * @Date 2019/5/30
 * @Version 1.0.0
 */
public class JwtUtils {
    private static final long EXPIRATION_TIME = 7 * 24 * 3600 * 1000;
    private static final String SECRET = "hello";
    private static final String TOKEN_PREFIX = "Bearer";
    private static final Key KEY = new SecretKeySpec(SECRET.getBytes(), SignatureAlgorithm.HS256.getJcaName());

    public static String generateToken(String userId, String name, String password, String avatar) {
        HashMap<String, Object> map = new HashMap<>(30);
        map.put("userId", userId);
        map.put("userName", name);
        map.put("password", password);
        map.put("avatar", avatar);
        map.put("type", "JWT");
        return Jwts.builder()
                .setClaims(map)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, KEY)
                .compact();
    }

    /**
     * request的token信息可以放在Header的Authorization字段 或者 Parameter的token字段 Cookie的token字段
     *
     * @param request
     *
     * @return
     */
    public static String getTokenFromRequest(HttpServletRequest request) {
        String token;
        if (null != (token = request.getHeader("Authorization"))) {
            return token;
        } else if (null != (token = request.getParameter("token"))) {
            return token;
        } else if (null != (CookieUtils.getValue(request, "token"))) {
            return token;
        } else {
            return null;
        }
    }


    /**
     * @param token
     *
     * @return Claims 可以使用get 获取Claims.get("name") Claims.get("password") Claims.get("userId")
     */
    public static Claims parseJWT(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(KEY)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();
            return claims;
        } catch (Exception ex) {
            throw new MyException(ResultEnum.TOKEN_WRONG);
        }
    }

    /**
     * @param token
     * @param key   定义返回的key值，可以是"name","password","userId","type"
     *
     * @return
     */
    public static Object parseJWT(String token, String key) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(KEY)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();
            return claims.get(key);
        } catch (Exception ex) {
            throw new MyException(ResultEnum.TOKEN_WRONG);
        }
    }


}
