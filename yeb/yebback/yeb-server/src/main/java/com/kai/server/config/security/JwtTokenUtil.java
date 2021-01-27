package com.kai.server.config.security;


import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    //根据用户信息生成token
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    //根据负载生成 jwt token
    private String generateToken(Map<String, Object> claim) {
        return Jwts.builder()
                .setClaims(claim)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.ES512, secret)
                .compact();
    }

    //从token中获取登录用户名
    public String getUserNameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    //判断token是否被刷新,过期即可刷新
    public boolean canRefresh(String token){
        return !isTokenExpired(token);
    }

    //刷新token
    public String refreshToken(String token){
        Claims claims = getClaimFromToken(token);
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generateToken(claims);
    }

    //验证token是否有效
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    //判断token是否失效
    private boolean isTokenExpired(String token) {
        Date expired = getExpiredDateFromToken(token);
        return expired.before(new Date());
    }

    //从token获取荷载时间
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimFromToken(token);
        return claims.getExpiration();
    }

    //从token中获取负载
    private Claims getClaimFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJwt(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
        }
        return claims;

    }

    //生成token失效时间
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }
}
