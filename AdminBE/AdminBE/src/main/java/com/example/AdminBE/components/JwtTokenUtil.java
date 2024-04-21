package com.example.AdminBE.components;

import com.example.AdminBE.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.expiration}")
    private long expiration ;//luu vao bien moi truong
    @Value("${jwt.secretKey}")
    private String secretKey;

    public String generateToken(com.example.AdminBE.entities.User user) throws Exception{
        Map<String,Object> claims = new HashMap<>();
        this.generateSecretKey();
        claims.put("phoneNumber", user.getPhoneNumber());
        try {
            String token= Jwts.builder()
                    .setClaims(claims) //lam the nao de ttrich xuat cac claim tu day
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis()+expiration*1000L))
                    .signWith(getSignInKey())
                    .compact();
            return token;
        }catch (Exception e){
            throw new InvalidParameterException("Khong the tao Jwt-token,error:"+e.getMessage());
        }

    }
    private Key getSignInKey(){
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // or HS384, HS512
        return key;
    }
    private String generateSecretKey(){
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32];
        random.nextBytes(keyBytes);
        String secretKey = Encoders.BASE64.encode(keyBytes);
        return secretKey;
    }
    private Claims extracAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolve){
        final Claims claims = this.extracAllClaims(token);
        return claimsResolve.apply(claims);
    }
    //check expiration
    private boolean isTokenExpired(String token){
        Date expirationDate = this.extractClaim(token,Claims::getExpiration);
        return expirationDate.before(new Date());
    }
}
