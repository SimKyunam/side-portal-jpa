package com.mile.portal.config.cache;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mile.portal.config.hibernateCollection.HibernateCollectionMixIn;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RedisCacheConfig {
    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.host}")
    private String host;

    public Jackson2JsonRedisSerializer redisJsonRedisSerializer() {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new JodaModule());
        objectMapper.addMixIn(Collection.class, HibernateCollectionMixIn.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        return jackson2JsonRedisSerializer;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(redisJsonRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public CacheManager redisCacheManager() {
        return RedisCacheManager
                .RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory())
                .cacheDefaults(redisCacheConfiguration(CacheProperties.DEFAULT_EXPIRE_SEC))
                .withInitialCacheConfigurations(customCacheConfig())
                .build();
    }

    // 커스텀 캐시들 추가
    public Map<String, RedisCacheConfiguration> customCacheConfig() {
        Map<String, Integer> cacheMap = new HashMap<>();
        cacheMap.put(CacheProperties.USER, CacheProperties.USER_EXPIRE_SEC);
        cacheMap.put(CacheProperties.BOARD_NOTICE, CacheProperties.BOARD_NOTICE_EXPIRE_SEC);
        cacheMap.put(CacheProperties.BOARD_FAQ, CacheProperties.BOARD_FAQ_EXPIRE_SEC);
        cacheMap.put(CacheProperties.BOARD_QNA, CacheProperties.BOARD_QNA_EXPIRE_SEC);
        cacheMap.put(CacheProperties.CODE, CacheProperties.CODE_EXPIRE_SEC);

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        cacheMap.forEach((key, value) -> {
            cacheConfigurations.put(key, redisCacheConfiguration(value));
        });

        return cacheConfigurations;
    }

    // RedisCacheConfiguration 공통처리
    public RedisCacheConfiguration redisCacheConfiguration(int expireSec) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues() // null 캐시 제외
                .entryTtl(Duration.ofSeconds(expireSec)) // 유지 시간 설정
                .computePrefixWith(CacheKeyPrefix.simple())
                .serializeKeysWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(redisJsonRedisSerializer()));
    }
}
