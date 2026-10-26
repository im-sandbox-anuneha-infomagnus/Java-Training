
package com.anuneha.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureDemo {

    public static void main(String[] args)
            throws InterruptedException, ExecutionException {

        CompletableFuture<Integer> fileA =
                CompletableFuture.supplyAsync(() -> {
                    System.out.println("Processing File A on "
                            + Thread.currentThread().getName());
                    return 120;
                });

        CompletableFuture<Integer> fileB =
                CompletableFuture.supplyAsync(() -> {
                    System.out.println("Processing File B on "
                            + Thread.currentThread().getName());
                    return 180;
                });

        CompletableFuture<Integer> totalLines =
                fileA.thenCombine(fileB, Integer::sum);

        CompletableFuture<String> report =
                totalLines.thenApply(total ->
                        "Total lines processed: " + total
                );

        System.out.println(report.get());
        System.out.println("Processing completed.");
    }
}
