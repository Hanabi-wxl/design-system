package edu.dlu.bysj.base.util;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/10 22:32
 */
public class JwtUtil {
    /**
     * 设置过期时间 30 分钟 ,暂时修改为 300分钟有效
     */
    private static final long TOKEN_EXPIRATION = 30 * 60 * 1000;

    /**
     * 设置加密的密钥
     */
    private static final String TOKEN_SIGN_KEY = "a8#@^a54f*&bc68a290b4dc!39";

    public static final String AUTH_HEADER = "JWT";

    /**
     * 生成token
     *
     * @param userId   :用户Id;
     * @param userName : 用户名称
     * @param userType :用户类型
     * @param majorId  :用户专业id
     * @param roleIds  : 角色id 数组
     * @return 返回token符串
     */
    public static String generateToken(
            Integer userId,
            String userName,
            String password,
            String userNumber,
            List<Integer> roleIds,
            String userType,
            Integer majorId,
            Integer schoolId) {
        return Jwts.builder()
                // 设置分组
                .setSubject("SYSTEM-USER")
                // 设置过期时间
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                .claim("userId", userId)
                .claim("userName", userName)
                .claim("userNumber", userNumber)
                .claim("roleIds", roleIds)
                .claim("userType", userType)
                .claim("majorId", majorId)
                .claim("schoolId", schoolId)
                .claim("password",password)
                // 加密
                .signWith(SignatureAlgorithm.HS512, TOKEN_SIGN_KEY)
                // 压缩
                .compressWith(CompressionCodecs.GZIP)
                .compact();
    }

    /**
     * 获取userId
     */
    public static Integer getUserId(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(TOKEN_SIGN_KEY).parseClaimsJws(token);

        Claims claims = claimsJws.getBody();
        return (Integer) claims.get("userId");
    }

    public static String getPassword(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(TOKEN_SIGN_KEY).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String) claims.get("password");
    }

    /**
     * 获取用户名称
     */
    public static String getUserName(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(TOKEN_SIGN_KEY).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String) claims.get("userName");
    }

    /**
     * 获取用户角色集合
     */
    public static List<Integer> getRoleIds(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(TOKEN_SIGN_KEY).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (ArrayList<Integer>) claims.get("roleIds");
    }

    /**
     * 获取角色类型 1 ： 老师, 0 学生;
     */
    public static String getUserType(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(TOKEN_SIGN_KEY).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String) claims.get("userType");
    }

    /**
     * 验证token
     */
    public static Jws<Claims> verify(String token) throws ExpiredJwtException, SignatureException {
        return Jwts.parser().setSigningKey(TOKEN_SIGN_KEY).parseClaimsJws(token);
    }

    /**
     * 刷新token 过期时间
     */
    public static String refreshToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        String userName = JwtUtil.getUserName(token);
        Integer userId = JwtUtil.getUserId(token);
        String userNumber = JwtUtil.getUserNumber(token);
        String userType = JwtUtil.getUserType(token);
        Integer majorId = JwtUtil.getMajorId(token);
        Integer schoolId = JwtUtil.getSchoolId(token);
        String password = JwtUtil.getPassword(token);
        List<Integer> array = JwtUtil.getRoleIds(token);
        return JwtUtil.generateToken(userId, userName,password, userNumber, array, userType, majorId, schoolId);
    }

    /**
     * 获取学号/教工号;
     *
     * @param token
     * @return
     */
    public static String getUserNumber(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(TOKEN_SIGN_KEY).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String) claims.get("userNumber");
    }

    /**
     * 获取majorId
     *
     * @param token jwt
     * @return 专业id
     */
    public static Integer getMajorId(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(TOKEN_SIGN_KEY).parseClaimsJws(token);
        Claims claim = claimsJws.getBody();
        return ((Integer) claim.get("majorId"));
    }

    /**
     * 获取学校id;
     *
     * @param token
     * @return
     */
    public static Integer getSchoolId(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(TOKEN_SIGN_KEY).parseClaimsJws(token);
        Claims claim = claimsJws.getBody();
        return ((Integer) claim.get("schoolId"));
    }
}
