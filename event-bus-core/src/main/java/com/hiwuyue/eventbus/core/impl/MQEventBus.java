package com.hiwuyue.eventbus.core.impl;

import com.alibaba.fastjson2.JSON;
import com.hiwuyue.eventbus.core.EventBus;
import com.hiwuyue.eventbus.core.EventBusCallback;
import com.hiwuyue.eventbus.core.EventTopic;
import com.hiwuyue.eventbus.core.PublishException;
import com.hiwuyue.eventbus.core.impl.mq.ClientBuildException;
import com.hiwuyue.eventbus.core.impl.mq.MQClient;
import com.hiwuyue.eventbus.core.impl.mq.MQClientBuilder;
import com.hiwuyue.eventbus.core.impl.mq.MQName;
import com.hiwuyue.eventbus.core.impl.mq.EventMessage;
import com.hiwuyue.eventbus.core.impl.mq.SendStatus;
import java.util.List;

public class MQEventBus extends MemoryEventBus implements EventBus {

    private final MQClient mqClient;

    public MQEventBus(MQName mqName) throws ClientBuildException {
        this.mqClient = new MQClientBuilder(mqName).build();
    }

    @Override
    public void subscribe(String topic, EventBusCallback callback) {
        registerMessageListener(topic);
        super.subscribe(topic, callback);
    }

    @Override
    public void subscribe(String topic, Class<? extends EventBusCallback> callbackClass) {
        registerMessageListener(topic);
        super.subscribe(topic, callbackClass);
    }

    @Override
    public void subscribeAsync(String topic, EventBusCallback callback) {
        registerMessageListener(topic);
        super.subscribeAsync(topic, callback);
    }

    @Override
    public void subscribeAsync(String topic, Class<? extends EventBusCallback> callbackClass) {
        registerMessageListener(topic);
        super.subscribeAsync(topic, callbackClass);
    }

    @Override
    public void subscribeOnce(String topic, EventBusCallback callback) {
        registerMessageListener(topic);
        super.subscribeOnce(topic, callback);
    }

    @Override
    public void subscribeOnce(String topic, Class<? extends EventBusCallback> callbackClass) {
        registerMessageListener(topic);
        super.subscribeOnce(topic, callbackClass);
    }

    @Override
    public void subscribeOnceAsync(String topic, Class<? extends EventBusCallback> callbackClass) {
        registerMessageListener(topic);
        super.subscribeOnceAsync(topic, callbackClass);
    }

    @Override
    public void subscribeOnceAsync(String topic, EventBusCallback callback) {
        registerMessageListener(topic);
        super.subscribeOnceAsync(topic, callback);
    }

    @Override
    public void clear() {
        this.mqClient.unsubscribe(this.topics().toArray(new String[0]));
        super.clear();
    }

    @Override
    public void clear(String topic) {
        this.mqClient.unsubscribe(topic);
        super.clear(topic);
    }

    @Override
    public void clearNow() {
        this.mqClient.unsubscribe(this.topics().toArray(new String[0]));
        super.clearNow();
    }

    @Override
    public void clearNow(String topic) {
        this.mqClient.unsubscribe(topic);
        super.clearNow(topic);
    }

    @Override
    public void publish(String topic, Object... args) throws PublishException {
        EventMessage eventMessage = new EventMessage(topic, JSON.toJSONString(args));
        SendStatus status = this.mqClient.send(eventMessage);
        if (status == SendStatus.FAIL) {
            throw new PublishException("send failed!");
        }
    }

    public String mqTopicName(String topic) {
        return "event_bus@" + topic;
    }

    private void registerMessageListener(String topic) {
        if (this.topics().contains(topic)) {
            return;
        }
        this.mqClient.subscribe(mqTopicName(topic), message -> {
            List<Object> args = JSON.parseObject(message.getBody(), List.class);
            EventTopic eventTopic = this.topicsTable.get(message.getTopic());
            if (eventTopic != null) {
                eventTopic.handle(args);
            }
        });
    }
}
