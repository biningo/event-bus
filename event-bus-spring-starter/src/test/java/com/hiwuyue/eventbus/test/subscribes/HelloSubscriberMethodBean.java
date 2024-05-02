package com.hiwuyue.eventbus.test.subscribes;

import com.hiwuyue.eventbus.spring.starter.EventBusSubscribe;
import com.hiwuyue.eventbus.test.Constants;
import org.springframework.stereotype.Component;

@Component
@EventBusSubscribe(topic = "HelloSubscriberMethodBean")
public class HelloSubscriberMethodBean {
    private MessageHolder messageHolder;

    public HelloSubscriberMethodBean(MessageHolder messageHolder) {
        this.messageHolder = messageHolder;
    }

    @EventBusSubscribe
    public void helloCallback() {
        this.messageHolder.setMessage(Constants.HELLO_WORLD);
    }
}
