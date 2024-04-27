package com.hiwuyue.eventbus.core;

public class EventBusSubscribeHelper {
    public static void subscribe(EventBus eventBus, String topic, boolean isAsync, boolean isOnce, EventBusCallback callback) {
        if (isAsync && isOnce) {
            eventBus.subscribeOnceAsync(topic, callback);
        } else if (isAsync) {
            eventBus.subscribeAsync(topic, callback);
        } else if (isOnce) {
            eventBus.subscribeOnce(topic, callback);
        } else {
            eventBus.subscribe(topic, callback);
        }
    }

    public static void subscribe(EventBus eventBus, String topic, boolean isAsync, boolean isOnce, Class<? extends EventBusCallback> callbackClass) {
        if (isAsync && isOnce) {
            eventBus.subscribeOnceAsync(topic, callbackClass);
        } else if (isAsync) {
            eventBus.subscribeAsync(topic, callbackClass);
        } else if (isOnce) {
            eventBus.subscribeOnce(topic, callbackClass);
        } else {
            eventBus.subscribe(topic, callbackClass);
        }
    }
}
