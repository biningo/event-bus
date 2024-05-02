package com.hiwuyue.eventbus.test.subscribes;

import org.springframework.stereotype.Component;

@Component
public class MessageHolder {
    private String message;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
