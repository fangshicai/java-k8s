package com.fsc.utils;

import com.fsc.config.JwtConfigProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    @Autowired
    private JwtConfigProperties jwtConfigProperties;

    public String getUUID() {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        return token;
    }

    /**
     * 生成jtw  jwt加密
     *
     * @param subject token中要存放的数据（json格式）
     * @return
     */
    public String createJWT(String subject) {
        System.out.println(jwtConfigProperties.getExpiration());
        JwtBuilder builder = getJwtBuilder(subject, jwtConfigProperties.getExpiration(), getUUID());// 设置过期时间
        return builder.compact();
    }

    /**
     * 生成jtw  jwt加密
     *
     * @param subject   token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return
     */
    public String createJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());// 设置过期时间
        return builder.compact();
    }

    private JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        return Jwts.builder()
                .setId(uuid)              //唯一的ID
                .setSubject(subject)   // 主题  可以是JSON数据
                .setIssuer("fsc")     // 签发者
                .setIssuedAt(new Date())      // 签发时间
                .signWith(signatureAlgorithm, secretKey) //使用HS256对称加密算法签名, 第二个参数为秘钥
                .setExpiration(new Date(System.currentTimeMillis()+ttlMillis));
    }


    public Claims parseJWT(String token) {
//        System.out.println(generalKey());
        return Jwts.parser()
                .setSigningKey(generalKey())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 生成加密后的秘钥 secretKey
     *
     * @return
     */
    public SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode("jwtConfigProperties");
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
//        System.out.println(key);
        return key;
    }

}
