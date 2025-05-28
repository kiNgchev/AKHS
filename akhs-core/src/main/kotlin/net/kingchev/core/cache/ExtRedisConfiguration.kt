package net.kingchev.core.cache

import net.kingchev.core.getenv
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
@EnableCaching
class ExtRedisConfiguration {
    private object RedisConnection {
        val host = getenv("REDIS_HOST") ?: "localhost"
        val port = getenv("REDIS_PORT")?.toIntOrNull() ?: 6379
    }

    @Bean
    fun redisConfiguration(): RedisStandaloneConfiguration {
        val config = RedisStandaloneConfiguration()
        config.hostName = RedisConnection.host
        config.port = RedisConnection.port
        return config
    }

    @Bean
    fun redisConnectionFactory(): LettuceConnectionFactory =
        LettuceConnectionFactory(redisConfiguration())

    @Bean
    fun redisCacheTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = GenericJackson2JsonRedisSerializer()
        template.connectionFactory = redisConnectionFactory
        return template
    }

    @Bean
    fun cacheManager(factory: RedisConnectionFactory): CacheManager {
        val redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(
                RedisSerializationContext
                    .SerializationPair
                    .fromSerializer(StringRedisSerializer())
            )
            .serializeValuesWith(
                RedisSerializationContext
                    .SerializationPair
                    .fromSerializer(GenericJackson2JsonRedisSerializer())
            )

        return RedisCacheManager.builder(factory)
            .cacheDefaults(redisCacheConfiguration)
            .build()
    }
}