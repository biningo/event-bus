package com.hiwuyue.eventbus.spring.starter;

import com.hiwuyue.eventbus.core.EventBus;
import com.hiwuyue.eventbus.core.impl.MemoryEventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventBusConfiguration {
    @Bean("eventBus")
    public EventBus memoryEventBus() {
        return new MemoryEventBus();
    }
}
