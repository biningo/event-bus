package com.hiwuyue.eventbus.core.impl.mq;

public interface MQClient {
    SendStatus send(EventMessage eventMessage);

    void subscribe(String topic, MessageListener messageListener);

    void unsubscribe(String... topics);
}
