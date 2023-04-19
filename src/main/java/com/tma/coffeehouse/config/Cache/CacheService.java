package com.tma.coffeehouse.config.Cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisCommandExecutionException;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CacheService {
    private final  CacheRepository cacheRepository;
    private final ObjectMapper objectMapper;
    public List<CacheData> findByKeyContainingIgnoreCase(String key) {
        List<CacheData> allData = (List<CacheData>) cacheRepository.findAll();
        System.out.println(allData);
        return allData.stream()
                .filter(data -> data.getKey().toLowerCase().contains(key.toLowerCase()))
                .collect(Collectors.toList());
    }
    public void writeCache(String key, Object results){
        try{
            String pageProductAsString = objectMapper.writeValueAsString(results);
            CacheData cacheData = new CacheData(key, pageProductAsString);
            cacheRepository.save(cacheData);
        }
        catch (JsonProcessingException | RedisSystemException | RedisException ignored ){
        }
    }
    public void destroyCache(String key){
        List<String> caches = this.findByKeyContainingIgnoreCase(key).stream()
                .map((CacheData::getKey))
                .toList();
        if (caches.isEmpty()) return;
        cacheRepository.deleteAllById(caches);
    }
}
