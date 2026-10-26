
package com.anuneha.concurrency;

public class ThreadCreationDemo {

    public static void main(String[] args) throws InterruptedException {

        Runnable task = () -> {
            System.out.println(
                "Task executed by: " +
                Thread.currentThread().getName()
            );
        };

        Thread worker = new Thread(task, "Worker-1");

        System.out.println("Before start(): " + worker.getState());

        worker.start();
        worker.join();

        System.out.println("After completion: " + worker.getState());
        System.out.println("Main thread: " +
                Thread.currentThread().getName());
    }
}
