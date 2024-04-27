package com.hiwuyue.eventbus.spring.starter;


import com.hiwuyue.eventbus.core.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventBusConfiguration {
    @Bean
    public EventBus eventBus() {
        return new EventBus();
    }
}
