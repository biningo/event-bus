package com.hiwuyue.eventbus.core.impl.mq;

import com.hiwuyue.eventbus.core.impl.mq.clientimpl.RocketMQClient;
import org.apache.rocketmq.client.exception.MQClientException;

public class MQClientBuilder {
    private MQName mqName;

    private String rocketMQNameSrv;

    public MQClientBuilder(MQName mqName) {
        this.mqName = mqName;
    }

    public MQClientBuilder setMQName(MQName mqName) {
        this.mqName = mqName;
        return this;
    }

    public MQClientBuilder setRocketMQNameSrv(String namesrv) {
        this.rocketMQNameSrv = namesrv;
        return this;
    }

    public MQClient build() throws ClientBuildException {
        switch (mqName) {
            case RocketMQ:
                return buildRocketMQClient();
            case Pulsar:
                return buildPulsarClient();
            case Kafka:
                return buildKafkaClient();
            default:
                throw new ClientBuildException("MQ name not exist");
        }
    }

    public MQClient buildRocketMQClient() throws ClientBuildException {
        try {
            return new RocketMQClient(this.rocketMQNameSrv);
        } catch (MQClientException e) {
            throw new ClientBuildException(e.getMessage());
        }
    }

    public MQClient buildKafkaClient() throws ClientBuildException {
        throw new ClientBuildException("unimplemented");
    }

    public MQClient buildPulsarClient() throws ClientBuildException {
        throw new ClientBuildException("unimplemented");
    }
}
