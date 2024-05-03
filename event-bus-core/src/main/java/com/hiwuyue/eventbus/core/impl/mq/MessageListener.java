package com.hiwuyue.eventbus.core.impl.mq;

public interface MessageListener {
    void consumeMessage(EventMessage eventMessage);
}
