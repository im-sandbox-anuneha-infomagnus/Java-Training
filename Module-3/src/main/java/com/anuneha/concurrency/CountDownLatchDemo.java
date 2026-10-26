
package com.anuneha.concurrency;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {

    public static void main(String[] args)
            throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(3);

        for (int i = 1; i <= 3; i++) {
            final int taskId = i;

            new Thread(() -> {
                try {
                    System.out.println(
                        "Task " + taskId + " started"
                    );
                    Thread.sleep(taskId * 500L);
                    System.out.println(
                        "Task " + taskId + " completed"
                    );
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            }).start();
        }

        latch.await();
        System.out.println("All tasks have finished.");
    }
}
