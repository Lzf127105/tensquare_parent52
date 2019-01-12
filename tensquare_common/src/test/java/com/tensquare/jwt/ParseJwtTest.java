package com.tensquare.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.text.SimpleDateFormat;

/**
 * @author Lzf
 * @create 2019-01-10 16:13
 * @Description: 解析JWT （Claims相当于一个map: key,vaules的形式）
 * 无状态
 */
public class ParseJwtTest {
    public static void main(String[] args) {
        try {
            String token ="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NjYiLCJzdWIiOiLlsI_pqawiLCJpYXQiOjE1NDcxMDkxMzksImV4cCI6MTU0NzEwOTE5OSwicm9sZSI6ImFkbWluIn0.sOez_RoQOnExNG_s5OKjkKaH4Eeat_TVgpTpKn5N93U";
            Claims claims =Jwts.parser().setSigningKey("itcast").parseClaimsJws(token).getBody();
            System.out.println("用户id:  "+claims.getId());
            System.out.println("用户名subject:  "+claims.getSubject());
            System.out.println("登录时间IssuedAt:   "+new SimpleDateFormat("yyyy-MM-ss HH:mm:ss").format(claims.getIssuedAt()));
            System.out.println("过期时间Expiration:   "+new SimpleDateFormat("yyyy-MM-ss HH:mm:ss").format(claims.getExpiration()));
            System.out.println("用户角色： "+claims.get("role")); //获取自定义字段
        }catch (Exception e){
            System.out.println("token已过期");
        }
    }
}
