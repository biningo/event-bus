package com.hiwuyue.eventbus.core;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class SyncWaitGroupTest {
    @Test
    public void testSyncWaitGroup() throws InterruptedException {
        {
            SyncWaitGroup wg = new SyncWaitGroup(0);
            boolean suc = wg.await(1000, TimeUnit.MILLISECONDS);
            Assert.assertTrue(suc);
        }

        {
            SyncWaitGroup wg = new SyncWaitGroup(1);
            boolean suc = wg.await(1000, TimeUnit.MILLISECONDS);
            Assert.assertFalse(suc);
        }

        {
            SyncWaitGroup wg = new SyncWaitGroup(0);
            int total = 5;
            wg.add(total);
            for (int idx = 0; idx < total; idx++) {
                wg.done();
            }
            boolean suc = wg.await(1000, TimeUnit.MILLISECONDS);
            Assert.assertTrue(suc);
        }

        {
            SyncWaitGroup wg = new SyncWaitGroup(0);
            int total = 10;
            for (int idx = 0; idx < total; idx++) {
                if (idx % 2 == 0) {
                    wg.add(1);
                }
            }
            Assert.assertEquals(total / 2, wg.getCount());

            boolean suc = wg.await(1000, TimeUnit.MILLISECONDS);
            Assert.assertFalse(suc);

            int count = wg.getCount();
            for (int idx = 0; idx < count; idx++) {
                wg.done();
            }
            suc = wg.await(1000, TimeUnit.MILLISECONDS);
            Assert.assertTrue(suc);
        }
    }
}
