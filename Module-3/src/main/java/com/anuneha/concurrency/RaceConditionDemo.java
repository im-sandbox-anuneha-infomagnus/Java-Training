
package com.anuneha.concurrency;

import java.util.concurrent.atomic.AtomicInteger;

public class RaceConditionDemo {

    private static final int ITERATIONS = 1_000_000;

    // Shared counter without protection
    static class UnsafeCounter {
        int count = 0;

        void increment() {
            count++;
        }
    }

    // Solution 1: synchronized
    static class SynchronizedCounter {
        private int count = 0;

        synchronized void increment() {
            count++;
        }

        int getCount() {
            return count;
        }
    }

    public static void main(String[] args)
            throws InterruptedException {

        // Demonstration: race condition
        UnsafeCounter unsafe = new UnsafeCounter();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                unsafe.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                unsafe.increment();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Expected count: "
                + (2 * ITERATIONS));
        System.out.println("Unsafe count:   "
                + unsafe.count);

        // Solution 1: synchronized
        SynchronizedCounter safe = new SynchronizedCounter();

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                safe.increment();
            }
        });

        Thread t4 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                safe.increment();
            }
        });

        t3.start();
        t4.start();

        t3.join();
        t4.join();

        System.out.println("Synchronized count: "
                + safe.getCount());

        // Solution 2: AtomicInteger
        AtomicInteger atomicCount = new AtomicInteger(0);

        Thread t5 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                atomicCount.incrementAndGet();
            }
        });

        Thread t6 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                atomicCount.incrementAndGet();
            }
        });

        t5.start();
        t6.start();

        t5.join();
        t6.join();

        System.out.println("Atomic count:       "
                + atomicCount.get());
    }
}
