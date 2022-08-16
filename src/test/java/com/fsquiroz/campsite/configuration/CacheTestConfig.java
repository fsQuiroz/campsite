package com.fsquiroz.campsite.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@EnableCaching
@Profile("test")
public class CacheTestConfig {
}
