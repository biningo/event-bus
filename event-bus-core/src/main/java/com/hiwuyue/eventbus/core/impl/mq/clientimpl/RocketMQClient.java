package com.hiwuyue.eventbus.core.impl.mq.clientimpl;

import com.hiwuyue.eventbus.core.impl.mq.EventMessage;
import com.hiwuyue.eventbus.core.impl.mq.MQClient;
import com.hiwuyue.eventbus.core.impl.mq.MessageListener;
import com.hiwuyue.eventbus.core.impl.mq.SendStatus;
import java.nio.charset.StandardCharsets;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RocketMQClient implements MQClient {
    private final String EVENT_BUS_CONSUMER_GROUP = "event-bus-consumer";
    private final String EVENT_BUS_PRODUCER_GROUP = "event-bus-producer";

    private final Logger LOG = LoggerFactory.getLogger(RocketMQClient.class);

    private final DefaultMQPushConsumer defaultMQPushConsumer;
    private final DefaultMQProducer defaultMQProducer;

    public RocketMQClient(String namesrv) throws MQClientException {
        this.defaultMQPushConsumer = new DefaultMQPushConsumer(namesrv);
        this.defaultMQPushConsumer.setConsumerGroup(EVENT_BUS_CONSUMER_GROUP);
        this.defaultMQPushConsumer.start();

        this.defaultMQProducer = new DefaultMQProducer(namesrv);
        this.defaultMQProducer.setProducerGroup(EVENT_BUS_PRODUCER_GROUP);
        this.defaultMQProducer.start();
    }

    @Override
    public SendStatus send(EventMessage eventMessage) {
        try {
            SendResult send = this.defaultMQProducer.send(convertToRMQMessage(eventMessage));
            if (send.getSendStatus() == org.apache.rocketmq.client.producer.SendStatus.SEND_OK) {
                return SendStatus.SUCCESS;
            }
        } catch (Exception err) {
            LOG.error("send event message fail:{}", err.getMessage());
        }
        return SendStatus.FAIL;
    }

    @Override
    public void subscribe(String topic, MessageListener messageListener) {
        try {
            this.defaultMQPushConsumer.subscribe(topic, "*");
            this.defaultMQPushConsumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
                for (MessageExt msg : msgs) {
                    messageListener.consumeMessage(convertToEventMessage(msg));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
        } catch (MQClientException e) {
            LOG.error("subscribe topic:{} fail. error:{}", topic, e.getMessage());
        }
    }

    @Override
    public void unsubscribe(String... topics) {
        for (String topic : topics) {
            this.defaultMQPushConsumer.unsubscribe(topic);
        }
    }

    private Message convertToRMQMessage(EventMessage eventMessage) {
        Message message = new Message();
        message.setTopic(eventMessage.getTopic());
        message.setBody(eventMessage.getBody().getBytes(StandardCharsets.UTF_8));
        return message;
    }

    private EventMessage convertToEventMessage(MessageExt messageExt) {
        EventMessage message = new EventMessage();
        message.setTopic(messageExt.getTopic());
        message.setBody(new String(messageExt.getBody()));
        return message;
    }
}
