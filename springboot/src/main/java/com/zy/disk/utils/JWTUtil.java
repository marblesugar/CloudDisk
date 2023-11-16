
package com.zy.disk.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;


public class JWTUtil {
    //三天过期
    private static final long expire = 3 * 24 * 60 * 60 *1000;
    //32位密钥
    private static final String secret = "sjidamskzixjcyfbeksoaldoskxisjab";
    //生成token
    public static String generateToken(Integer id){
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expire);
        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setSubject(""+id)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    //解析token
    public static Claims getClaimsByToken(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}

