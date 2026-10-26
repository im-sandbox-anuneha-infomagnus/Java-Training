
package com.anuneha.concurrency;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BoundedQueueDemo {

    public static void main(String[] args)
            throws InterruptedException {

        ThreadPoolExecutor executor =
                new ThreadPoolExecutor(
                    2,
                    2,
                    0,
                    TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(2),
                    new ThreadPoolExecutor.CallerRunsPolicy()
                );

        for (int i = 1; i <= 8; i++) {
            final int taskId = i;

            executor.execute(() -> {
                System.out.println(
                    "Task " + taskId + " running on "
                    + Thread.currentThread().getName()
                );

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("All tasks finished.");
    }
}
