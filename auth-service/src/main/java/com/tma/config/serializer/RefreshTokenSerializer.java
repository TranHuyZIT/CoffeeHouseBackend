package com.tma.config.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tma.Refreshtoken.RefreshToken;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.IOException;

public class RefreshTokenSerializer implements RedisSerializer<RefreshToken> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public byte[] serialize(RefreshToken refreshToken) throws SerializationException {
        try {
             return objectMapper.writeValueAsBytes(refreshToken);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RefreshToken deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) return null;
        try {
            return objectMapper.readValue(bytes, new TypeReference<RefreshToken>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
