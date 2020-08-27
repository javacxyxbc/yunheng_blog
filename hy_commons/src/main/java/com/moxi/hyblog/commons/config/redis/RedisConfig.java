package com.moxi.hyblog.commons.config.redis;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;



@Configuration

@ConditionalOnClass(RedisOperations.class)

@EnableConfigurationProperties(RedisProperties.class)

public class RedisConfig {



    @Bean

    @ConditionalOnMissingBean(name = "redisTemplate")

    public RedisTemplate<Object, Object> redisTemplate(

            RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<Object, Object> template = new RedisTemplate<>();



        //使用fastjson序列化

        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);

        // value值的序列化采用fastJsonRedisSerializer

        template.setValueSerializer(fastJsonRedisSerializer);

        template.setHashValueSerializer(fastJsonRedisSerializer);

        // key的序列化采用StringRedisSerializer

        template.setKeySerializer(new StringRedisSerializer());

        template.setHashKeySerializer(new StringRedisSerializer());



        template.setConnectionFactory(redisConnectionFactory);

        return template;

    }



    @Bean

    @ConditionalOnMissingBean(StringRedisTemplate.class)

    public StringRedisTemplate stringRedisTemplate(

            RedisConnectionFactory redisConnectionFactory) {

        StringRedisTemplate template = new StringRedisTemplate();

        template.setConnectionFactory(redisConnectionFactory);

        return template;

    }



}


