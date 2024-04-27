package com.hiwuyue.eventbus.core;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventBusHandler {
    private EventBusCallback callback;
    private final boolean isCallback;

    private final Class<? extends EventBusCallback> callbackClass;

    private Boolean once = false;
    private Boolean async = false;

    public EventBusHandler(Class<? extends EventBusCallback> callbackClass) {
        this.callbackClass = callbackClass;
        this.isCallback = false;
    }

    public EventBusHandler(EventBusCallback callback) {
        this.isCallback = true;
        this.callback = callback;
        this.callbackClass = callback.getClass();
    }

    public EventBusHandler once() {
        this.once = true;
        return this;
    }

    public EventBusHandler async() {
        this.async = true;
        return this;
    }

    public Boolean isOnce() {
        return once;
    }

    public Boolean isAsync() {
        return async;
    }

    public Class<? extends EventBusCallback> getEventBusCallbackClass() {
        return this.callbackClass;
    }

    public EventBusCallback getCallback() {
        return this.callback;
    }

    public void handle(Object... args) throws ReflectiveOperationException {
        if (isCallback) {
            callback.run();
            return;
        }
        Optional<Constructor<? extends EventBusCallback>> constructorOpt = findEventBusCallbackConstructor(args);
        if (constructorOpt.isEmpty()) {
            return;
        }
        if (callback == null) {
            callback = constructorOpt.get().newInstance(args);
        }
        callback.run();
    }

    private Optional<Constructor<? extends EventBusCallback>> findEventBusCallbackConstructor(Object... args) {
        List<Class<?>> paramClasses = new ArrayList<>();
        for (Object arg : args) {
            paramClasses.add(arg.getClass());
        }

        try {
            Constructor<? extends EventBusCallback> constructor = this.callbackClass.getConstructor(paramClasses.toArray(new Class[0]));
            return Optional.of(constructor);
        } catch (NoSuchMethodException ignored) {
            return Optional.empty();
        }
    }
}
