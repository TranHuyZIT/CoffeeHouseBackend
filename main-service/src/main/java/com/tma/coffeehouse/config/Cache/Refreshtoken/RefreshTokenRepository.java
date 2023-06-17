package com.tma.coffeehouse.config.Cache.Refreshtoken;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
    private final RedisTemplate<String, RefreshToken> redisTemplate;
    boolean checkExistRefreshToken(String refreshTokenString){
        return Boolean.TRUE.equals(redisTemplate.hasKey(refreshTokenString));
    }
    void saveRefreshToken(String refreshTokenString){
        redisTemplate.opsForValue().set(refreshTokenString, new RefreshToken(refreshTokenString, "refreshToken"));
    }

}
