package com.hiwuyue.eventbus.core;

public interface EventBusPublisher {
    void publish(String topic, Object... args) throws ReflectiveOperationException;
}
