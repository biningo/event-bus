package com.hiwuyue.eventbus.spring.starter;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.*;

/**
 * reference: https://github.com/dromara/dynamic-tp/blob/master/common/src/main/java/org/dromara/dynamictp/common/spring/SpringBeanHelper.java
 */
public class SpringBeanHelper {
    private SpringBeanHelper() {
    }

    public static void register(BeanDefinitionRegistry registry,
                                String beanName,
                                Class<?> clazz,
                                Map<String, Object> propertyValues,
                                Object... constructorArgs) {
        register(registry, beanName, clazz, propertyValues, null, constructorArgs);
    }

    public static void register(BeanDefinitionRegistry registry,
                                String beanName,
                                Class<?> clazz,
                                Map<String, Object> propertyValues,
                                List<String> dependsOnBeanNames,
                                Object... constructorArgs) {
        if (ifPresent(registry, beanName, clazz) || registry.containsBeanDefinition(beanName)) {
            registry.removeBeanDefinition(beanName);
        }
        doRegister(registry, beanName, clazz, propertyValues, dependsOnBeanNames, constructorArgs);
    }

    public static void registerIfAbsent(BeanDefinitionRegistry registry,
                                        String beanName,
                                        Class<?> clazz,
                                        Object... constructorArgs) {
        registerIfAbsent(registry, beanName, clazz, null, null, constructorArgs);
    }

    public static void registerIfAbsent(BeanDefinitionRegistry registry,
                                        String beanName,
                                        Class<?> clazz,
                                        Map<String, Object> propertyValues,
                                        Object... constructorArgs) {
        registerIfAbsent(registry, beanName, clazz, propertyValues, null, constructorArgs);
    }

    public static void registerIfAbsent(BeanDefinitionRegistry registry,
                                        String beanName,
                                        Class<?> clazz,
                                        Map<String, Object> propertyValues,
                                        List<String> dependsOnBeanNames,
                                        Object... constructorArgs) {
        if (!ifPresent(registry, beanName, clazz) && !registry.containsBeanDefinition(beanName)) {
            doRegister(registry, beanName, clazz, propertyValues, dependsOnBeanNames, constructorArgs);
        }
    }

    public static boolean ifPresent(BeanDefinitionRegistry registry, String beanName, Class<?> clazz) {
        String[] beanNames = getBeanNames((ListableBeanFactory) registry, clazz);
        return Arrays.asList(beanNames).contains(beanName);
    }

    public static String[] getBeanNames(ListableBeanFactory beanFactory, Class<?> clazz) {
        return beanFactory.getBeanNamesForType(clazz, true, false);
    }

    private static void doRegister(BeanDefinitionRegistry registry,
                                   String beanName,
                                   Class<?> clazz,
                                   Map<String, Object> propertyValues,
                                   List<String> dependsOnBeanNames,
                                   Object... constructorArgs) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        for (Object constructorArg : constructorArgs) {
            builder.addConstructorArgValue(constructorArg);
        }
        if (propertyValues != null) {
            propertyValues.forEach(builder::addPropertyValue);
        }
        if (dependsOnBeanNames != null) {
            dependsOnBeanNames.forEach(builder::addDependsOn);
        }
        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }
}
