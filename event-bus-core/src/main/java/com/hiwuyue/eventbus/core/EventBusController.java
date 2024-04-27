package com.hiwuyue.eventbus.core;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public interface EventBusController {
    List<String> topics();

    void waitAsync(String topic) throws InterruptedException;

    void waitAsync(String topic, long timeout, TimeUnit timeUnit) throws InterruptedException;

    void asyncEventExecutor(String topic, ThreadPoolExecutor executor);

    void clear();

    void clear(String topic);

    void clearNow();

    void clearNow(String topic);
}
