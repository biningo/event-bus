package com.hiwuyue.eventbus.core.impl.mq;

public interface MQClient {
    SendStatus send(Message message);

    void subscribe(String topic, MessageListener messageListener);

    void unsubscribe(String... topic);
}
