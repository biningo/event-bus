package com.hiwuyue.eventbus.core;

public interface EventBusCallbackSubscriber {
    void subscribe(String topic, EventBusCallback callback);

    void subscribeAsync(String topic, EventBusCallback callback);

    void subscribeOnce(String topic, EventBusCallback callback);

    void subscribeOnceAsync(String topic, EventBusCallback callback);

    void unSubscribe(String topic, EventBusCallback callback);
}
