package com.hiwuyue.eventbus.core;

import com.hiwuyue.eventbus.core.impl.MemoryEventBus;
import org.junit.Assert;
import org.junit.Test;

public class MemoryEventBusTest {

    @Test
    public void testCallbackSubscribe() throws ReflectiveOperationException {
        EventBus bus = new MemoryEventBus();
        String topic = "test";
        CountHolder countHolder = new CountHolder();

        int count = 100;
        bus.subscribe(topic, () -> {
            for (int i = 0; i < count; i++) {
                countHolder.add(1);
            }
        });

        int total = 1000;
        for (int i = 0; i < total; i++) {
            bus.publish(topic);
        }
        Assert.assertEquals(total * count, countHolder.getCount());
    }

    @Test
    public void testCallbackSubscribeAsync() throws ReflectiveOperationException {
        EventBus bus = new MemoryEventBus();
        String topic = "test";
        CountHolder countHolder = new CountHolder();

        long waitMs = 1000;
        int count = 100;
        bus.subscribeAsync(topic, () -> {
            sleepIgnoreInterrupt(waitMs);
            for (int i = 0; i < count; i++) {
                countHolder.add(1);
            }
        });

        long start = System.currentTimeMillis();
        int total = 20;
        for (int i = 0; i < total; i++) {
            bus.publish(topic);
        }
        Assert.assertTrue((System.currentTimeMillis() - start) < waitMs);

        try {
            bus.waitAsync(topic);
        } catch (InterruptedException ignored) {
        }
        Assert.assertEquals(count * total, countHolder.getCount());
    }

    @Test
    public void testCallbackSubscribeOnce() throws ReflectiveOperationException {
        EventBus bus = new MemoryEventBus();
        String topic = "test";
        CountHolder countHolder = new CountHolder();

        int count = 100;
        bus.subscribeOnce(topic, () -> {
            for (int i = 0; i < count; i++) {
                countHolder.add(1);
            }
        });

        int total = 1000;
        for (int i = 0; i < total; i++) {
            bus.publish(topic);
        }
        Assert.assertEquals(count, countHolder.getCount());
    }

    @Test
    public void testCallbackSubscribeOnceAsync() throws ReflectiveOperationException {
        EventBus bus = new MemoryEventBus();
        String topic = "test";
        CountHolder countHolder = new CountHolder();

        long waitMs = 1000;
        int count = 100;
        bus.subscribeOnceAsync(topic, () -> {
            sleepIgnoreInterrupt(waitMs);
            for (int i = 0; i < count; i++) {
                countHolder.add(1);
            }
        });

        long start = System.currentTimeMillis();
        int total = 20;
        for (int i = 0; i < total; i++) {
            bus.publish(topic);
        }
        Assert.assertTrue((System.currentTimeMillis() - start) < waitMs);

        try {
            bus.waitAsync(topic);
        } catch (InterruptedException ignored) {
        }
        Assert.assertEquals(count, countHolder.getCount());
    }

    @Test
    public void testCallbackClassSubscribe() throws ReflectiveOperationException {
        EventBus bus = new MemoryEventBus();
        String topic = "test";
        bus.subscribe(topic, CountCallback.class);

        CountHolder countHolder = new CountHolder();
        int count = 100;
        int total = 1000;
        for (int i = 0; i < total; i++) {
            bus.publish(topic, countHolder, count);
        }
        Assert.assertEquals(total * count, countHolder.getCount());
    }

    @Test
    public void testCallbackClassSubscribeOnce() throws ReflectiveOperationException {
        EventBus bus = new MemoryEventBus();
        String topic = "test";
        CountHolder countHolder = new CountHolder();

        int count = 100;
        bus.subscribeOnce(topic, CountCallback.class);

        int total = 1000;
        for (int i = 0; i < total; i++) {
            bus.publish(topic, countHolder, count);
        }
        Assert.assertEquals(count, countHolder.getCount());
    }

    @Test
    public void testCallbackClassSubscribeAsync() throws InterruptedException, ReflectiveOperationException {
        EventBus bus = new MemoryEventBus();
        String topic = "test";
        bus.subscribeAsync(topic, CountCallback.class);

        CountHolder countHolder = new CountHolder();
        int count = 100;
        int total = 20;
        for (int i = 0; i < total; i++) {
            bus.publish(topic, countHolder, count);
        }
        bus.waitAsync(topic);
        Assert.assertEquals(total * count, countHolder.getCount());
    }

    @Test
    public void testCallbackClassSubscribeOnceAsync() throws InterruptedException, ReflectiveOperationException {
        EventBus bus = new MemoryEventBus();
        String topic = "test";
        bus.subscribeOnceAsync(topic, CountCallback.class);

        CountHolder countHolder = new CountHolder();
        int count = 100;
        int total = 20;
        for (int i = 0; i < total; i++) {
            bus.publish(topic, countHolder, count);
        }
        bus.waitAsync(topic);
        Assert.assertEquals(count, countHolder.getCount());
    }

    @Test
    public void testCallbackUnsubscribe() throws ReflectiveOperationException {
        EventBus bus = new MemoryEventBus();
        String topic = "test";
        CountHolder countHolder = new CountHolder();
        int targetCount = 0;

        EventBusCallback callback = () -> countHolder.add(targetCount);
        bus.subscribe(topic, callback);
        bus.publish(topic);

        bus.unSubscribe(topic, callback);
        bus.publish(topic);
        bus.publish(topic);

        Assert.assertEquals(targetCount, countHolder.getCount());
    }

    @Test
    public void testCallbackClassUnsubscribe() throws ReflectiveOperationException {
        EventBus bus = new MemoryEventBus();
        String topic = "test";
        CountHolder countHolder = new CountHolder();
        int targetCount = 0;

        bus.subscribe(topic, CountCallback.class);
        bus.publish(topic, countHolder, targetCount);

        bus.unSubscribe(topic, CountCallback.class);
        bus.publish(topic, countHolder, targetCount);
        bus.publish(topic, countHolder, targetCount);

        Assert.assertEquals(targetCount, countHolder.getCount());
    }

    @Test
    public void testEventBusClear() throws ReflectiveOperationException {
        EventBus bus = new MemoryEventBus();
        String topic = "test";
        CountHolder countHolder = new CountHolder();
        int targetCount = 0;

        bus.subscribe(topic, () -> countHolder.add(targetCount));
        bus.clear(topic);
        bus.publish(topic);
        Assert.assertEquals(0, countHolder.getCount());

        bus.subscribe(topic, () -> countHolder.add(targetCount));
        bus.clear();
        bus.publish(topic);
        Assert.assertEquals(0, countHolder.getCount());
    }

    private static class CountCallback implements EventBusCallback {
        private final int count;
        private final CountHolder countHolder;

        public CountCallback(CountHolder countHolder, Integer count) {
            this.count = count;
            this.countHolder = countHolder;
        }

        @Override
        public void run() {
            for (int idx = 0; idx < count; idx++) {
                countHolder.add(1);
            }
        }
    }

    public static void sleepIgnoreInterrupt(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }

}
