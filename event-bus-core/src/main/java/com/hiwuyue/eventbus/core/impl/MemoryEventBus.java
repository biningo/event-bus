package com.hiwuyue.eventbus.core.impl;

import com.hiwuyue.eventbus.core.EventBus;
import com.hiwuyue.eventbus.core.EventBusCallback;
import com.hiwuyue.eventbus.core.EventBusHandler;
import com.hiwuyue.eventbus.core.EventTopic;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MemoryEventBus implements EventBus {
    private final Map<String, EventTopic> topics = new ConcurrentHashMap<>();

    @Override
    public void asyncEventExecutor(String topic, ThreadPoolExecutor executor) {
        EventTopic eventTopic = getEventTopic(topic);
        eventTopic.setExecutor(executor);
    }

    @Override
    public void publish(String topic, Object... args) {
        if (!this.topics.containsKey(topic)) {
            return;
        }
        this.topics.get(topic).handle(args);
    }

    @Override
    public void subscribe(String topic, Class<? extends EventBusCallback> callbackClass) {
        EventTopic eventTopic = getEventTopic(topic);
        eventTopic.addEventBusHandler(new EventBusHandler(callbackClass));
    }

    @Override
    public void subscribe(String topic, EventBusCallback callback) {
        EventTopic eventTopic = getEventTopic(topic);
        eventTopic.addEventBusHandler(new EventBusHandler(callback));
    }

    @Override
    public void subscribeAsync(String topic, Class<? extends EventBusCallback> callbackClass) {
        EventBusHandler handler = new EventBusHandler(callbackClass).async();
        EventTopic eventTopic = getEventTopic(topic);
        eventTopic.addEventBusHandler(handler);
    }

    @Override
    public void subscribeAsync(String topic, EventBusCallback callback) {
        EventBusHandler handler = new EventBusHandler(callback).async();
        EventTopic eventTopic = getEventTopic(topic);
        eventTopic.addEventBusHandler(handler);
    }

    @Override
    public void subscribeOnce(String topic, Class<? extends EventBusCallback> callbackClass) {
        EventBusHandler handler = new EventBusHandler(callbackClass).once();
        EventTopic eventTopic = getEventTopic(topic);
        eventTopic.addEventBusHandler(handler);
    }

    @Override
    public void subscribeOnce(String topic, EventBusCallback callback) {
        EventBusHandler handler = new EventBusHandler(callback).once();
        EventTopic eventTopic = getEventTopic(topic);
        eventTopic.addEventBusHandler(handler);
    }

    @Override
    public void subscribeOnceAsync(String topic, Class<? extends EventBusCallback> callbackClass) {
        EventBusHandler handler = new EventBusHandler(callbackClass).async().once();
        EventTopic eventTopic = getEventTopic(topic);
        eventTopic.addEventBusHandler(handler);
    }

    @Override
    public void subscribeOnceAsync(String topic, EventBusCallback callback) {
        EventBusHandler handler = new EventBusHandler(callback).async().once();
        EventTopic eventTopic = getEventTopic(topic);
        eventTopic.addEventBusHandler(handler);
    }

    @Override
    public void unSubscribe(String topic, Class<? extends EventBusCallback> callbackClass) {
        if (!this.topics.containsKey(topic)) {
            return;
        }
        List<EventBusHandler> handlers = this.topics.get(topic).getHandlers();
        List<EventBusHandler> handlersCopy = new ArrayList<>(handlers);
        for (EventBusHandler handler : handlersCopy) {
            if (handler.getEventBusCallbackClass().getName().equals(callbackClass.getName())) {
                handlers.remove(handler);
            }
        }
    }

    @Override
    public void unSubscribe(String topic, EventBusCallback callback) {
        if (!this.topics.containsKey(topic)) {
            return;
        }
        List<EventBusHandler> handlers = this.topics.get(topic).getHandlers();
        List<EventBusHandler> handlersCopy = new ArrayList<>(handlers);
        for (EventBusHandler handler : handlersCopy) {
            if (handler.getCallback().hashCode() == callback.hashCode()) {
                handlers.remove(handler);
            }
        }
    }

    @Override
    public List<String> topics() {
        return new ArrayList<>(this.topics.keySet());
    }

    @Override
    public void waitAsync(String topic) throws InterruptedException {
        if (!this.topics.containsKey(topic)) {
            return;
        }
        this.topics.get(topic).waitAsync();
    }

    @Override
    public void waitAsync(String topic, long timeout, TimeUnit timeUnit) throws InterruptedException {
        if (!this.topics.containsKey(topic)) {
            return;
        }
        this.topics.get(topic).waitAsync(timeout, timeUnit);
    }

    @Override
    public void clear() {
        HashMap<String, EventTopic> topicsCopy = new HashMap<>(this.topics);
        topicsCopy.forEach((topicName, eventTopic) -> {
            eventTopic.clear();
            this.topics.remove(topicName);
        });
    }

    @Override
    public void clear(String topic) {
        EventTopic eventTopic = this.topics.remove(topic);
        if (eventTopic != null) {
            eventTopic.clear();
        }
    }

    @Override
    public void clearNow() {
        HashMap<String, EventTopic> topicsCopy = new HashMap<>(this.topics);
        topicsCopy.forEach((topicName, eventTopic) -> {
            eventTopic.clearNow();
            this.topics.remove(topicName);
        });
    }

    @Override
    public void clearNow(String topic) {
        EventTopic eventTopic = this.topics.remove(topic);
        if (eventTopic != null) {
            eventTopic.clearNow();
        }
    }

    private EventTopic getEventTopic(String topic) {
        this.topics.putIfAbsent(topic, new EventTopic(topic));
        return topics.get(topic);
    }
}
