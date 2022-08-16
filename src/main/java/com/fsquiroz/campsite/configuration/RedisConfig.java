package com.fsquiroz.campsite.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
@Profile("!test")
public class RedisConfig {

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(
            @Value("${redis.host:localhost}") String host,
            @Value("${redis.port:9736}") int port
    ) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }
}
