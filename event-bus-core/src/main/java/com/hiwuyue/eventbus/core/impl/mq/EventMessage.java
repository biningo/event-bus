package com.hiwuyue.eventbus.core.impl.mq;

public class EventMessage {
    private String topic;

    private String body;

    public EventMessage(String topic, String body) {
        this.topic = topic;
        this.body = body;
    }

    public EventMessage() {}

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
