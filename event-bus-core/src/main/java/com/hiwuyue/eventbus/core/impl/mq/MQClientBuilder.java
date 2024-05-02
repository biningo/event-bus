package com.hiwuyue.eventbus.core.impl.mq;

public class MQClientBuilder {
    private MQClient mqClient;

    private MQName mqName;

    public MQClientBuilder(MQName mqName) {
        this.mqName = mqName;
    }

    public MQClientBuilder setMQName(MQName mqName) {
        this.mqName = mqName;
        return this;
    }

    public MQClient build() {
        switch (mqName) {
            case RocketMQ:
            case Pulsar:
            case Kafka:
            default:
                return this.mqClient;
        }
    }
}
