package com.tma.coffeehouse.config.Cache;

import com.tma.coffeehouse.config.Cache.CacheData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CacheRepository extends CrudRepository<CacheData, String> {
    List<CacheData> findByKeyContainingIgnoreCase(String key);
}