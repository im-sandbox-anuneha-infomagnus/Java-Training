package com.hona.module_21;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {

    @Bean
    public String appVersion() {
        return "Hona Smart Config v1.0";
    }
    @Bean
    @Lazy
    public java.util.UUID requestId() {
        return java.util.UUID.randomUUID();
    }
    @Bean
    @Primary
    public String defaultGreeting() {
        return "Hello from Primary!";
    }

    @Bean
    @Qualifier("specialGreeting")
    public String specialGreeting() {
        return "Hello from Special!";
    }
    @Bean
    @Scope("prototype")
    public java.util.Date prototypeDate() {
        return new java.util.Date();
    }
}