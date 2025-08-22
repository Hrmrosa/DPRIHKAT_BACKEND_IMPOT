package com.DPRIHKAT.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.DPRIHKAT.repository")
public class JpaConfig {
}
