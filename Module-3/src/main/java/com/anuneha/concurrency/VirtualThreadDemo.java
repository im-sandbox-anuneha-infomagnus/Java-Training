
package com.anuneha.concurrency;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class VirtualThreadDemo {

    public static void main(String[] args)
            throws Exception {

        try (var executor =
                     Executors.newVirtualThreadPerTaskExecutor()) {

            Future<?> first = executor.submit(() ->
                System.out.println(
                    "Task 1: " +
                    Thread.currentThread()
                )
            );

            Future<?> second = executor.submit(() ->
                System.out.println(
                    "Task 2: " +
                    Thread.currentThread()
                )
            );

            first.get();
            second.get();
        }

        System.out.println("Both virtual-thread tasks completed.");
    }
}
