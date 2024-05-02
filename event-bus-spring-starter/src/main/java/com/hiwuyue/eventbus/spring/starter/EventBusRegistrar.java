package com.hiwuyue.eventbus.spring.starter;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;
import java.util.Map;

public class EventBusRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                importingClassMetadata.getAnnotationAttributes(EnableEventBus.class.getName()));

        registerEventBusConfiguration(registry);
        registerEventSubscribeScanner(registry, attributes.getStringArray("scanPackages"));
    }

    private void registerEventBusConfiguration(BeanDefinitionRegistry registry) {
        SpringBeanHelper.register(registry, "eventBusConfiguration", EventBusConfiguration.class, null);
    }

    private void registerEventSubscribeScanner(BeanDefinitionRegistry registry, String[] scanPackages) {
        Map<String, Object> propertyValues = Maps.newHashMap();
        propertyValues.put("scanPackages", scanPackages);
        SpringBeanHelper.register(registry, "eventSubscribeScanner", EventSubscribeScanner.class, propertyValues, List.of("eventBus"));
    }
}
