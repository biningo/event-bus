package com.hiwuyue.eventbus.test;

import com.hiwuyue.eventbus.core.EventBusCallback;
import com.hiwuyue.eventbus.spring.starter.ClassLoaderHelper;
import com.hiwuyue.eventbus.test.subscribes.HelloSubscriber;
import com.hiwuyue.eventbus.test.subscribes.HelloSubscriberBean;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ClassLoaderHelperTest {
    @Test
    public void testScanClasses() {
        String[] scanPackages = new String[]{"com.hiwuyue.eventbus.test.subscribes"};
        List<Class<?>> classes = new ArrayList<>();
        try {
            classes = ClassLoaderHelper.scanClasses(scanPackages, EventBusCallback.class);
        } catch (ClassNotFoundException e) {
            Assert.fail(e.toString());
        }
        Assert.assertEquals(2, classes.size());
        Assert.assertTrue(classes.contains(HelloSubscriber.class));
        Assert.assertTrue(classes.contains(HelloSubscriberBean.class));
    }
}
