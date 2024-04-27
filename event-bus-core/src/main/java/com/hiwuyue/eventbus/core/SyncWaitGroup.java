package com.hiwuyue.eventbus.core;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class SyncWaitGroup extends AbstractQueuedSynchronizer {
    public SyncWaitGroup() {
    }

    public SyncWaitGroup(int count) {
        setState(count);
    }

    int getCount() {
        return getState();
    }

    public void await() throws InterruptedException {
        this.acquireSharedInterruptibly(1);
    }

    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return this.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }

    protected int tryAcquireShared(int acquires) {
        return (getState() == 0) ? 1 : -1;
    }

    public void done() {
        this.releaseShared(1);
    }

    public void add(int count) {
        for (; ; ) {
            int prevc = getState();
            int nextc = prevc + count;
            if (compareAndSetState(prevc, nextc)) {
                return;
            }
        }
    }

    protected boolean tryReleaseShared(int releases) {
        // Decrement count; signal when transition to zero
        for (; ; ) {
            int c = getState();
            if (c == 0)
                return false;
            int nextc = c - 1;
            if (compareAndSetState(c, nextc))
                return nextc == 0;
        }
    }
}
