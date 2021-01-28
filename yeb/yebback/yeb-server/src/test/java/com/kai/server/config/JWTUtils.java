package com.kai.server.config;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

public class JWTUtils {

    private static final String SIGN = "!@#$%^";

    /**
     * 生成token header.payload.sign
     */
    public static String getToken(Map<String, String> map) {

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 7);//默认7天过期

        //创建jwt builder
        JWTCreator.Builder builder = JWT.create();
        //payload
        map.forEach((k, v) -> {
            builder.withClaim(k, v);
        });

        //指定过期时间
        builder.withExpiresAt(instance.getTime());
        //sign
        String token = builder.sign(Algorithm.HMAC256(SIGN));

        return token;
    }

    /**
     * 验证token
     */
    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
    }

//    /**
//     * 获取信息
//     */
//    public static DecodedJWT getTokenInfo(String token){
//        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
//        return verify;
//    }
}
