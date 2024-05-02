package com.hiwuyue.eventbus.test.subscribes;

import com.hiwuyue.eventbus.core.EventBusCallback;
import com.hiwuyue.eventbus.spring.starter.EventBusSubscribe;
import com.hiwuyue.eventbus.test.Constants;

@EventBusSubscribe(topic = "HelloSubscriber")
public class HelloSubscriber implements EventBusCallback {
    private final MessageHolder messageHolder;

    public HelloSubscriber(MessageHolder messageHolder) {
        this.messageHolder = messageHolder;
    }

    @Override
    public void run() {
        this.messageHolder.setMessage(Constants.HELLO_WORLD);
    }
}
