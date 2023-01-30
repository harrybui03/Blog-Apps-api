package com.example.demo.security;


import com.example.demo.exception.BlogAPIException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app-jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    //generate JWT token
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String token = Jwts.builder().setSubject(username).setIssuedAt(currentDate).setExpiration(expireDate).signWith(key()).compact();

        return token;
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    //get username from JWT token
    public String getUsername(String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();

        String username = claims.getSubject();
        return username;
    }

    //validate JWT token
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException ex){
            throw new BlogAPIException("Invalid JWT token", HttpStatus.BAD_REQUEST);
        } catch (ExpiredJwtException ex){
            throw new BlogAPIException("Expired JWT token" , HttpStatus.BAD_REQUEST);
        }catch (UnsupportedJwtException ex){
            throw new BlogAPIException("Unsupported JWT token", HttpStatus.BAD_REQUEST);
        }catch (IllegalArgumentException ex){
            throw new BlogAPIException("JWT claims string is empty", HttpStatus.BAD_REQUEST);
        }
    }

}
