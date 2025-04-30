package com.delivery.delivery.core.jwt;

import com.delivery.delivery.core.entity.User;
import com.delivery.delivery.core.exception.DefaultException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@Component
//@RequiredArgsConstructor
//public class JwtTokenUtils {
//
//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.lifetime}")
//    private Duration jwtLifetime;
//
//    public String generateToken(User user){
//        Map<String, Object> claims = new HashMap<>();
//
//        Date issuedDate = new Date();
//        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(user.getEmail())
//                .claim("userId", user.getId().toString())
//                .setIssuedAt(issuedDate)
//                .setExpiration(expiredDate)
//                .signWith(getSignKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public Long getUserIdFromToken(String token){
//        return Long.parseLong(getAllClaimsFromToken(token).get("userId", String.class));
//    }
//
//    public LocalDate getExpirationDateFromToken(String token) {
//        Date expirationDate = getAllClaimsFromToken(token).getExpiration();
//        return expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//    }
//
//    private SecretKey getSignKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secret);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    public Long getUserIdFromAuthentication(Authentication authentication) {
//        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
//            if (userDetails instanceof User) {
//                return ((User) userDetails).getId();
//            }
//        }
//        throw new DefaultException("Cannot extract user ID from Authentication object");
//    }
//
//    private Claims getAllClaimsFromToken(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSignKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//}
