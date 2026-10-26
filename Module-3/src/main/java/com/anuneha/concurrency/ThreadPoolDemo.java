
package com.anuneha.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolDemo {

    private static void processTask(String taskName) {
        System.out.println(
            taskName + " started on "
            + Thread.currentThread().getName()
        );

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(taskName + " was interrupted.");
            return;
        }

        System.out.println(taskName + " completed.");
    }

    public static void main(String[] args)
            throws InterruptedException {

        ExecutorService executor =
                Executors.newFixedThreadPool(3);

        for (int i = 1; i <= 10; i++) {
            String taskName = "Task-" + i;

            executor.submit(() -> processTask(taskName));
        }

        executor.shutdown();

        boolean finished = executor.awaitTermination(
                15, TimeUnit.SECONDS
        );

        if (finished) {
            System.out.println("All tasks completed.");
        } else {
            System.out.println("Some tasks are still running.");
            executor.shutdownNow();
        }
    }
}
