package com.DPRIHKAT.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(JacksonConfig.class)
public class AppConfig {
    // Configuration principale de l'application
}
