package com.example.myapplication.model;

import java.util.concurrent.atomic.AtomicInteger;

public class NotificationID {
    private final static AtomicInteger c = new AtomicInteger(0);
    public static int getID() {
        System.out.println("Atomic Number"+ c.get());
        return c.incrementAndGet();
    }
    public static int getActualID(){
        return c.get();
    }
}
