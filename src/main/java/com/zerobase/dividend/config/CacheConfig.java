package com.zerobase.dividend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class CacheConfig {//레디스 캐시를 사용하기 위한 Bean 설정

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory){
        //직렬화 : 데이터와 오브젝트 같은 값들을 바이트 형태로 변환 -> 자바에서만 호환되던 데이터들을 다른 곳에서도 사용할 수 있도록 변환
        RedisCacheConfiguration conf = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
            //.entryTtl() 데이터의 유효기간 설정가능
            ;

        return RedisCacheManager.RedisCacheManagerBuilder
                                .fromConnectionFactory(redisConnectionFactory)
                                .cacheDefaults(conf)
                                .build();
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){ //레디스와 커넥션을 맺을 수 있는 팩토리만 설정된 상태
        RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration(); //클러스터나 센티넬은 다르게 설정
        conf.setHostName(this.host);
        conf.setPort(this.port);

        return new LettuceConnectionFactory(conf);
    }

}
