package com.foodDelivery.FoodDeliveryApp.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());

    }

    public String extractEmail(String Token){
        return getClaims(Token).getSubject();
    }
    public boolean validateToken(String email,String Token){
        return extractEmail(Token).equals(email) && !isTokenExpired(Token);
    }

    private boolean isTokenExpired(String Token) {
        return getClaims(Token).getExpiration().before(new Date());
    }


    private Claims getClaims(String Token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(Token)
                .getBody();


    }


}
