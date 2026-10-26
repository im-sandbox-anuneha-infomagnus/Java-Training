
package com.anuneha.concurrency;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentMapDemo {

    public static void main(String[] args)
            throws InterruptedException {

        ConcurrentHashMap<String, Integer> counts =
                new ConcurrentHashMap<>();

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                counts.merge("Java", 1, Integer::sum);
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Java count: " + counts.get("Java"));
    }
}
