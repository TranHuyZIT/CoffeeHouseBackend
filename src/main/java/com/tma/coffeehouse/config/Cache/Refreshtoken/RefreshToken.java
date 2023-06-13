package com.tma.coffeehouse.config.Cache.Refreshtoken;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tma.coffeehouse.config.Serializer.RefreshTokenSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
@AllArgsConstructor
@Getter
@Accessors(chain = true)
@RedisHash(value = "cacheData", timeToLive = 60*60*24)
@NoArgsConstructor
public class RefreshToken {
    @Id
    private String key;

    @Indexed
    private String value;
}
