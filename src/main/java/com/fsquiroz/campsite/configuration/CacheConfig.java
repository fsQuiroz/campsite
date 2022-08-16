package com.fsquiroz.campsite.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@EnableCaching
@Profile("!test")
public class CacheConfig {

    public static final String AVAILABLE_DAYS_CACHE = "availableDays";

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(
            @Value("${cache.availableDays.ttlMinutes:1}") int availableDaysTtlMinutes
    ) {
        return (builder) -> builder.withCacheConfiguration(
                AVAILABLE_DAYS_CACHE,
                defaultCacheConfiguration().entryTtl(Duration.of(availableDaysTtlMinutes, ChronoUnit.MINUTES))
        );
    }

    private RedisCacheConfiguration defaultCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer()
                ));
    }
}
