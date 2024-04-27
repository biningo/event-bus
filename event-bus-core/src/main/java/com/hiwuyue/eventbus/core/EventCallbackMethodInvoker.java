package com.hiwuyue.eventbus.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventCallbackMethodInvoker implements EventBusCallback {
    private final Object bindObject;
    private final Method method;
    private final Object[] args;

    public EventCallbackMethodInvoker(Object bindObject, Method method, Object... args) {
        this.bindObject = bindObject;
        this.method = method;
        this.args = args;
    }

    public void invoke() throws InvocationTargetException, IllegalAccessException {
        this.method.invoke(bindObject, args);
    }

    @Override
    public void run() {
        try {
            this.invoke();
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
