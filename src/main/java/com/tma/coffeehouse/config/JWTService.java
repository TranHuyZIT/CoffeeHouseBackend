package com.tma.coffeehouse.config;

import com.tma.coffeehouse.ExceptionHandling.CustomException;
import com.tma.coffeehouse.config.Cache.Refreshtoken.RefreshTokenRepository;
import com.tma.coffeehouse.config.Cache.Refreshtoken.RefreshTokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JWTService {
    private final String SECRET_KEY = "5368566D597133743677397A24432646294A404E635166546A576E5A72347537";
    private final String SECRET_REFRESH_KEY = "ES8SNaJqxSQ16RvVsDkMDuZrbjLiSfZMsAePtFS9edjbY37920suNnQ5nSe7uKDJ\n";
    private final RefreshTokenService refreshTokenService;
    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);
    }
    public String extractUserNameRefreshToken(String refreshToken){
        return extractRefreshTokenClaim(refreshToken, Claims::getSubject);
    }

    public boolean isTokenValid (String token, UserDetails userDetails){
        if (token == null) return false;
        try{
            final String username = extractUserName(token);
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        }
        catch (MalformedJwtException e){
            System.out.println("Token Malformed!");
        }
        catch (ExpiredJwtException e) {
            System.out.println("Token expired!");
        }
        catch (UnsupportedJwtException e){
            System.out.println("Token unsupported!");
        }
        catch(IllegalArgumentException e){
            System.out.println("JWT claims string is empty");
        }
        return false;
    }
    public boolean isRefreshTokenValid(String refreshToken){
        try{
            if (refreshToken == null) return false;
            extractAllClaimsRefreshToken(refreshToken);
            return refreshTokenService.checkRefreshToken(refreshToken);
        }
        catch (SignatureException e){
            System.out.println("Invalid signature");
        }
        catch (MalformedJwtException e){
            System.out.println("Token Malformed!");
        }
        catch (ExpiredJwtException e) {
            System.out.println("Token expired!");
        }
        catch (UnsupportedJwtException e){
            System.out.println("Token unsupported!");
        }
        catch(IllegalArgumentException e){
            System.out.println("JWT claims string is empty");
        }
        return false;
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    public boolean isRefreshTokenExpired(String refreshToken){
        return extractRefreshExpiration(refreshToken).before(new Date());
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
    public Date extractRefreshExpiration(String token){
        return extractRefreshTokenClaim(token, Claims::getExpiration);
    }
    public String signToken(UserDetails userDetails){
        return signToken(new HashMap<>(), userDetails);
    }
    public String signToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 900000)).signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String signRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getSecretRefreshKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String signRefreshToken(UserDetails userDetails){
        return signRefreshToken(new HashMap<>(), userDetails);
    }
    public <T> T extractClaim(String token, Function <Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public <T> T extractRefreshTokenClaim(String token, Function <Claims, T> claimsResolver){
        final Claims claims = extractAllClaimsRefreshToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();

    }
    private Claims extractAllClaimsRefreshToken(String refreshToken){
        return Jwts.parserBuilder().setSigningKey(getSecretRefreshKey()).build().parseClaimsJws(refreshToken).getBody();
    }
    private Key getSecretKey(){
        byte[] KeyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(KeyBytes);
    }
    private Key getSecretRefreshKey(){
        byte[] KeyBytes = Decoders.BASE64.decode(SECRET_REFRESH_KEY);
        return Keys.hmacShaKeyFor(KeyBytes);
    }

}
