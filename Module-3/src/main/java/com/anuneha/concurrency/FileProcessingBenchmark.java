
package com.anuneha.concurrency;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class FileProcessingBenchmark {

    private static final Path DATA_DIR =
            Path.of("sample-files");

    private static List<Path> createFiles() throws Exception {
        Files.createDirectories(DATA_DIR);
        List<Path> files = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Path file = DATA_DIR.resolve("file" + i + ".txt");
            Files.writeString(file,
                    ("Java concurrency example\n").repeat(1000));
            files.add(file);
        }

        return files;
    }

    private static long processFile(Path file) throws Exception {
        // Read the file and count its lines.
        long lines;
        try (var reader = Files.lines(file)) {
            lines = reader.count();
        }

        // Simulate additional processing time.
        Thread.sleep(100);

        return lines;
    }

    public static void main(String[] args) throws Exception {
        List<Path> files = createFiles();

        long sequentialStart = System.nanoTime();
        long sequentialTotal = 0;

        for (Path file : files) {
            sequentialTotal += processFile(file);
        }

        long sequentialTime = System.nanoTime() - sequentialStart;

        long parallelStart = System.nanoTime();
        long parallelTotal = 0;

        ExecutorService executor =
                Executors.newFixedThreadPool(3);

        try {
            List<Future<Long>> results = new ArrayList<>();

            for (Path file : files) {
                results.add(executor.submit(() -> processFile(file)));
            }

            for (Future<Long> result : results) {
                parallelTotal += result.get();
            }
        } finally {
            executor.shutdown();
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        }

        long parallelTime = System.nanoTime() - parallelStart;

        System.out.println("Files processed: " + files.size());
        System.out.println("Sequential total lines: " + sequentialTotal);
        System.out.println("Parallel total lines:   " + parallelTotal);
        System.out.printf("Sequential time: %.2f ms%n",
                sequentialTime / 1_000_000.0);
        System.out.printf("Parallel time:   %.2f ms%n",
                parallelTime / 1_000_000.0);
    }
}
