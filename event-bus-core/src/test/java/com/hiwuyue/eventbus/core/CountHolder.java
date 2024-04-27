package com.hiwuyue.eventbus.core;

public class CountHolder {
    private int count = 0;

    public synchronized int add(int val) {
        count += val;
        return count;
    }

    public int getCount() {
        return count;
    }
}
