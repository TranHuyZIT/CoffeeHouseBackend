package com.tma.coffeehouse.config;

import com.tma.coffeehouse.ExceptionHandling.CustomException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {
    private final String SECRET_KEY = "5368566D597133743677397A24432646294A404E635166546A576E5A72347537";
    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid (String token, UserDetails userDetails){
        if (token == null) return false;
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
    public String signToken(UserDetails userDetails){
        return signToken(new HashMap<>(), userDetails);
    }
    public String signToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)).signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public <T> T extractClaim(String token, Function <Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token){
        try{
            return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();
        }
        catch (ExpiredJwtException e){
            throw new CustomException("JWT expired", HttpStatus.UNAUTHORIZED);
        }
    }
    public void throwExpiredError(String authHeader,String token){
        throw new ExpiredJwtException(null, this.extractAllClaims(token), "");
    }
    private Key getSecretKey(){
        byte[] KeyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(KeyBytes);
    }
}
