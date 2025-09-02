package com.shanzhu.hospital.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Map;

/**
 * Jwt工具类 (什么是Jwt ? https://blog.csdn.net/qq_51133939/article/details/126330855)
 *

 */
@Slf4j
public class JwtUtil {

    /**
     * 签名 盐
     */
    private static String SIGNAL = "1HU&**UUY**(GNH";

    /**
     * Jwt token 生成
     *
     * @param map 需要生成的数据
     * @return Jwt token
     */
    public static String generateToken(Map<String, String> map) {
        //设置过期时间为30天
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 30);

        //创建jwt builder
        final JWTCreator.Builder builder = JWT.create();

        //Jwt play load
        map.forEach((k, v) -> {
            builder.withClaim(k, v);
        });

        //指定令牌过期时间
        String token = builder.withExpiresAt(instance.getTime())
                //sign签名
                .sign(Algorithm.HMAC256(SIGNAL));

        return token;
    }

    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(SIGNAL)).build().verify(token);
    }

}
