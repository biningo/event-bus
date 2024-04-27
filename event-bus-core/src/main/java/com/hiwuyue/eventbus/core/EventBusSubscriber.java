package com.hiwuyue.eventbus.core;

public interface EventBusSubscriber {
    void subscribe(String topic, Class<? extends EventBusCallback> callbackClass);

    void subscribeAsync(String topic, Class<? extends EventBusCallback> callbackClass);

    void subscribeOnce(String topic, Class<? extends EventBusCallback> callbackClass);

    void subscribeOnceAsync(String topic, Class<? extends EventBusCallback> callbackClass);

    void unSubscribe(String topic, Class<? extends EventBusCallback> callbackClass);
}
