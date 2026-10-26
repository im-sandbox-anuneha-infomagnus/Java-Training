
package com.anuneha.concurrency;

public class ConcurrencyDemo {

    private static void doTask(String taskName)
            throws InterruptedException {

        for (int i = 1; i <= 3; i++) {
            System.out.println(
                taskName + " - Step " + i
                + " | Thread: "
                + Thread.currentThread().getName()
            );

            Thread.sleep(200);
        }
    }

    public static void main(String[] args)
            throws InterruptedException {

        Thread taskA = new Thread(
            () -> {
                try {
                    doTask("Task A");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            },
            "Worker-A"
        );

        Thread taskB = new Thread(
            () -> {
                try {
                    doTask("Task B");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            },
            "Worker-B"
        );

        taskA.start();
        taskB.start();

        taskA.join();
        taskB.join();

        System.out.println("Both tasks completed.");
    }
}
