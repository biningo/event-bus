package com.hiwuyue.eventbus.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class EventTopic {
    private final String topicName;

    private final List<EventBusHandler> handlers = Collections.synchronizedList(new ArrayList<>());

    private ThreadPoolExecutor executor;

    private final SyncWaitGroup syncWaitGroup = new SyncWaitGroup();


    public EventTopic(String topicName, ThreadPoolExecutor executor) {
        this.topicName = topicName;
        this.executor = executor;
    }

    public EventTopic(String topicName) {
        this.topicName = topicName;
        this.executor = defaultEventExecutor();
    }

    public List<EventBusHandler> getHandlers() {
        return this.handlers;
    }

    public void addEventBusHandler(EventBusHandler handler) {
        this.handlers.add(handler);
    }

    public void setExecutor(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    public void handleEventAsync(EventBusHandler handler, Object... args) {
        this.syncWaitGroup.add(1);
        this.executor.submit(() -> {
            try {
                handler.handle(args);
            } catch (ReflectiveOperationException ignored) {
            }
            this.syncWaitGroup.done();
        });
    }

    public void handleEvent(EventBusHandler handler, Object... args) {
        try {
            handler.handle(args);
        } catch (ReflectiveOperationException ignored) {
        }
    }

    public synchronized void handle(Object... args) {
        ArrayList<EventBusHandler> handlersCopy = new ArrayList<>(this.handlers);
        for (EventBusHandler handler : handlersCopy) {
            if (handler.isOnce()) {
                this.handlers.remove(handler);
            }
            if (handler.isAsync()) {
                handleEventAsync(handler, args);
                return;
            }
            handleEvent(handler, args);
        }
    }

    public void waitAsync() throws InterruptedException {
        this.syncWaitGroup.await();
    }

    public void waitAsync(long timeout, TimeUnit timeUnit) throws InterruptedException {
        this.syncWaitGroup.await(timeout, timeUnit);
    }

    private ThreadPoolExecutor defaultEventExecutor() {
        return new ThreadPoolExecutor(20, 20,
                0, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new ThreadFactoryBuilder().setNameFormat("memory-event-bus-async-%d").build(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    public void clear() {
        this.executor.shutdown();
        try {
            waitAsync(3, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
        }
        this.executor.shutdownNow();
    }

    public void clearNow() {
        this.executor.shutdownNow();
    }
}
