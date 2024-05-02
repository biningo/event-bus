package com.hiwuyue.eventbus.spring.starter;

import org.springframework.context.annotation.Bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface EventBusSubscribe {

    String topic() default "";

    boolean async() default false;

    boolean once() default false;
}
