package com.hiwuyue.eventbus.spring.starter;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(EventBusRegistrar.class)
public @interface EnableEventBus {
    String[] scanPackages() default {};
}
