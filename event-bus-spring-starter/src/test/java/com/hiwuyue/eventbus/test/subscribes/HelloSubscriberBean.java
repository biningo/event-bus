package com.hiwuyue.eventbus.test.subscribes;

import com.hiwuyue.eventbus.core.EventBusCallback;
import com.hiwuyue.eventbus.spring.starter.EventBusSubscribe;
import com.hiwuyue.eventbus.test.Constants;
import org.springframework.stereotype.Component;

@Component
@EventBusSubscribe(topic = "HelloSubscriberBean")
public class HelloSubscriberBean implements EventBusCallback {
    private MessageHolder messageHolder;

    public HelloSubscriberBean(MessageHolder messageHolder) {
        this.messageHolder = messageHolder;
    }

    @Override
    public void run() {
        this.messageHolder.setMessage(Constants.HELLO_WORLD);
    }
}
