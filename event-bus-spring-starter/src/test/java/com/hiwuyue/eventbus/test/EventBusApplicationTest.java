package com.hiwuyue.eventbus.test;


import com.hiwuyue.eventbus.core.EventBus;
import com.hiwuyue.eventbus.core.impl.MemoryEventBus;
import com.hiwuyue.eventbus.spring.starter.EnableEventBus;
import com.hiwuyue.eventbus.spring.starter.EventBusConfiguration;
import com.hiwuyue.eventbus.spring.starter.EventSubscribeScanner;
import com.hiwuyue.eventbus.test.subscribes.HelloSubscriber;
import com.hiwuyue.eventbus.test.subscribes.HelloSubscriberBean;
import com.hiwuyue.eventbus.test.subscribes.HelloSubscriberMethodBean;
import com.hiwuyue.eventbus.test.subscribes.MessageHolder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;


@EnableEventBus(scanPackages = "com.hiwuyue.eventbus.test.subscribes")
@SpringBootApplication(scanBasePackages = "com.hiwuyue.eventbus.test.subscribes")
@SpringBootTest(classes = EventBusApplicationTest.class)
public class EventBusApplicationTest {
    private ApplicationContext applicationContext;

    @Before
    public void setUp() {
        applicationContext = SpringApplication.run(EventBusApplicationTest.class);
    }

    @Test
    public void testEnableEventBus() {
        EventBusConfiguration eventBusConfiguration = applicationContext.getBean(EventBusConfiguration.class);
        Assert.assertNotNull(eventBusConfiguration);

        EventBus eventBus = applicationContext.getBean(MemoryEventBus.class);
        Assert.assertNotNull(eventBus);

        EventSubscribeScanner eventSubscribeScanner = applicationContext.getBean(EventSubscribeScanner.class);
        Assert.assertNotNull(eventSubscribeScanner);
        Assert.assertEquals(eventBus, eventSubscribeScanner.getEventBus());
        Assert.assertArrayEquals(new String[]{"com.hiwuyue.eventbus.test.subscribes"}, eventSubscribeScanner.getScanPackages());
    }

    @Test
    public void testSubscribe() throws ReflectiveOperationException {
        MessageHolder messageHolder = applicationContext.getBean(MessageHolder.class);
        EventBus eventBus = applicationContext.getBean(MemoryEventBus.class);

        messageHolder.setMessage("");
        eventBus.publish(HelloSubscriber.class.getSimpleName(), messageHolder);
        Assert.assertEquals(Constants.HELLO_WORLD, messageHolder.getMessage());

        messageHolder.setMessage("");
        eventBus.publish(HelloSubscriberBean.class.getSimpleName());
        Assert.assertEquals(Constants.HELLO_WORLD, messageHolder.getMessage());

        messageHolder.setMessage("");
        eventBus.publish(HelloSubscriberMethodBean.class.getSimpleName());
        Assert.assertEquals(Constants.HELLO_WORLD, messageHolder.getMessage());
    }
}
