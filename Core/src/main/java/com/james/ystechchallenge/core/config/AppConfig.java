package com.james.ystechchallenge.core.config;

import com.james.ystechchallenge.core.constant.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Contains app-wide configurations
 * @author james
 */
@Configuration
public class AppConfig {
    
    /**
     * @return How long it takes for a confirmed accreditation request to expire in seconds.
     */
    @Bean
    public int expirationDurationSeconds() {
        return Constants.EXPIRATION_DURATION_SECONDS;
    }
}