
package com.anuneha.concurrency;

public class ThreadLifecycleDemo {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Main thread started.");

        Thread worker = new Thread(() -> {
            System.out.println("Worker thread started.");

            for (int i = 1; i <= 5; i++) {
                System.out.println("Worker: " + i);
            }

            System.out.println("Worker thread finished.");
        });

        worker.start();
        worker.join();

        System.out.println("Main thread finished.");
    }
}
