package com.ernestas.zedgebackend.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

  public static final String ARTIST_SEARCH_CACHE = "artistSearchCache";

  @Value("${zedge.backend.artist.cache.expiry.time.hours:1}")
  private Long artistCacheExpiryTime;

  @Bean
  public CacheManager cacheManager() {
    SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
    simpleCacheManager.setCaches(Collections.singletonList(artistSearchCache()));
    return simpleCacheManager;
  }

  @Bean
  public CaffeineCache artistSearchCache() {
    return new CaffeineCache(ARTIST_SEARCH_CACHE,
        Caffeine.newBuilder().expireAfterWrite(artistCacheExpiryTime, TimeUnit.HOURS)
            .build());
  }


}
