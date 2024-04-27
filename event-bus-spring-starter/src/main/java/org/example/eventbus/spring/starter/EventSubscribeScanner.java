package com.hiwuyue.eventbus.spring.starter;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import com.hiwuyue.eventbus.core.EventBus;
import com.hiwuyue.eventbus.core.EventBusCallback;
import com.hiwuyue.eventbus.core.EventBusSubscribeHelper;
import com.hiwuyue.eventbus.core.EventCallbackMethodInvoker;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventSubscribeScanner implements ApplicationContextAware, InitializingBean {

    private String[] scanPackages;
    private EventBus eventBus;
    private ApplicationContext applicationContext;

    public EventSubscribeScanner() {
    }

    public EventSubscribeScanner(EventBus eventBus, String[] scanPackages) {
        this.eventBus = eventBus;
        this.scanPackages = scanPackages;
    }

    public void setScanPackages(String[] scanPackages) {
        this.scanPackages = scanPackages;
    }

    public String[] getScanPackages() {
        return this.scanPackages;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public EventBus getEventBus() {
        return this.eventBus;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.eventBus = this.applicationContext.getBean(EventBus.class);

        Map<String, Object> subscribers = this.applicationContext.getBeansWithAnnotation(EventBusSubscribe.class);
        subscribeEventBusSubscribeBeans(subscribers.values().toArray());

        List<Class<? extends EventBusCallback>> callbackClasses = new ArrayList<>();
        for (Class<?> clazz : ClassLoaderHelper.scanClasses(scanPackages, EventBusCallback.class)) {
            if (!this.applicationContext.getBeansOfType(clazz).isEmpty()) {
                continue;
            }
            callbackClasses.add((Class<? extends EventBusCallback>) clazz);
        }
        subscribeEventBusCallbackClasses(callbackClasses);
    }

    @VisibleForTesting
    private void subscribeEventBusSubscribeBeans(Object[] beans) {
        for (Object bean : beans) {
            EventBusSubscribe subscribeAnnotation = bean.getClass().getAnnotation(EventBusSubscribe.class);
            String topic = subscribeAnnotation.topic();
            boolean isAsync = subscribeAnnotation.async();
            boolean isOnce = subscribeAnnotation.once();

            if (bean instanceof EventBusCallback) {
                EventBusCallback callback = (EventBusCallback) bean;
                EventBusSubscribeHelper.subscribe(eventBus, topic, isAsync, isOnce, callback);
                continue;
            }

            Method[] methods = bean.getClass().getMethods();
            for (Method method : methods) {
                subscribeAnnotation = method.getAnnotation(EventBusSubscribe.class);
                if (subscribeAnnotation == null) {
                    continue;
                }
                String methodTopic = subscribeAnnotation.topic();
                if (StringUtils.isNotEmpty(methodTopic)) {
                    topic = methodTopic;
                }
                isAsync = isAsync || subscribeAnnotation.async();
                isOnce = isOnce || subscribeAnnotation.once();
                EventBusSubscribeHelper.subscribe(eventBus, topic, isAsync, isOnce, new EventCallbackMethodInvoker(bean, method));
            }
        }
    }

    private void subscribeEventBusCallbackClasses(List<Class<? extends EventBusCallback>> classes) {
        for (Class<? extends EventBusCallback> callbackClass : classes) {
            EventBusSubscribe subscribeAnnotation = callbackClass.getAnnotation(EventBusSubscribe.class);
            String topic = subscribeAnnotation.topic();
            boolean isAsync = subscribeAnnotation.async();
            boolean isOnce = subscribeAnnotation.once();
            EventBusSubscribeHelper.subscribe(eventBus, topic, isAsync, isOnce, callbackClass);
        }
    }
}
