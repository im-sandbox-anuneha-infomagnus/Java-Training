package com.hona.filetransfer;

import com.hona.filetransfer.integrity.FileHasher;
import com.hona.filetransfer.merger.ChunkMerger;
import com.hona.filetransfer.splitter.ChunkSplitter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public final class HonaFileProcessingDemo {

    private static final long DEFAULT_CHUNK_SIZE = 4 * 1024;
    private static final int SAMPLE_FILE_SIZE = 10 * 1024;

    private HonaFileProcessingDemo() {
    }

    public static void main(String[] args) throws IOException {

        Path demoRoot = Path.of("demo-data");
        Files.createDirectories(demoRoot);

        Path runDirectory =
                Files.createTempDirectory(demoRoot, "run-");

        Path inputDirectory = runDirectory.resolve("input");
        Path chunksDirectory = runDirectory.resolve("chunks");
        Path outputDirectory = runDirectory.resolve("output");

        Files.createDirectories(inputDirectory);
        Files.createDirectories(outputDirectory);

        Path sourceFile;

        if (args.length >= 1) {
            sourceFile = Path.of(args[0]).toAbsolutePath();

            if (!Files.isRegularFile(sourceFile)) {
                throw new IOException("Input file does not exist");
            }
        } else {
            sourceFile = inputDirectory.resolve("sample.bin");
            createDemoFile(sourceFile);
        }

        long chunkSize = DEFAULT_CHUNK_SIZE;

        if (args.length >= 2) {
            long chunkSizeMiB = Long.parseLong(args[1]);

            if (chunkSizeMiB <= 0) {
                throw new IllegalArgumentException(
                        "Chunk size must be positive"
                );
            }

            chunkSize = Math.multiplyExact(
                    chunkSizeMiB,
                    1024L * 1024L
            );
        }

        Path mergedFile =
                outputDirectory.resolve("reconstructed.bin");

        System.out.println();
        System.out.println("======================================");
        System.out.println("       HONA FILE PROCESSING DEMO");
        System.out.println("======================================");

        System.out.println("\nDEMO WORKSPACE");
        System.out.println(runDirectory.toAbsolutePath());

        System.out.println("\n[1] ORIGINAL FILE");
        System.out.println("File: " + sourceFile);
        System.out.println("File size: " + Files.size(sourceFile) + " bytes");
        System.out.println("Chunk size: " + chunkSize + " bytes");

        System.out.println("\n[2] SPLITTING");

        ChunkSplitter splitter = new ChunkSplitter();

        long totalChunks = splitter.split(
                sourceFile,
                chunksDirectory,
                chunkSize
        );

        System.out.println("Splitting completed!");
        System.out.println("Total chunks: " + totalChunks);

        printChunkFiles(chunksDirectory);

        System.out.println("\n[3] MERGING");

        ChunkMerger merger = new ChunkMerger();

        merger.merge(
                chunksDirectory,
                mergedFile,
                Files.size(sourceFile),
                chunkSize
        );

        System.out.println("Merging completed!");
        System.out.println("Merged size: " + Files.size(mergedFile));

        System.out.println("\n[4] SHA-256 VERIFICATION");

        String originalHash =
                FileHasher.calculateSha256(sourceFile);

        String mergedHash =
                FileHasher.calculateSha256(mergedFile);

        System.out.println("Original SHA-256: " + originalHash);
        System.out.println("Merged SHA-256:   " + mergedHash);

        if (!originalHash.equals(mergedHash)) {
            throw new IOException(
                    "Integrity verification failed"
            );
        }

        System.out.println("INTEGRITY STATUS: MATCH");

        System.out.println("\n======================================");
        System.out.println("       HONA PROCESS COMPLETED");
        System.out.println("======================================");

        System.out.println("Generated files: "
                + runDirectory.toAbsolutePath());
    }

    private static void createDemoFile(Path sourceFile)
            throws IOException {

        byte[] data = new byte[SAMPLE_FILE_SIZE];

        for (int index = 0; index < data.length; index++) {
            data[index] = (byte) (index % 256);
        }

        Files.write(sourceFile, data);
    }

    private static void printChunkFiles(Path chunksDirectory)
            throws IOException {

        List<Path> chunkFiles;

        try (Stream<Path> paths = Files.list(chunksDirectory)) {
            chunkFiles = paths
                    .sorted(Comparator.comparing(
                            path -> path.getFileName().toString()
                    ))
                    .toList();
        }

        for (Path chunk : chunkFiles) {
            System.out.printf(
                    "  %-18s %,d bytes%n",
                    chunk.getFileName(),
                    Files.size(chunk)
            );
        }
    }
}
//mvn clean test
//mvn compile exec:java
//explorer .\demo-data
/*to upload file the comands are 
mvn compile exec:java '-Dexec.args=C:/HonaDemo/my-input.txt.txt'
 */
/*to ge the length of the file 
(Get-Item "C:\HonaDemo\my-input.txt.txt").Length */
/*Chunk size: maximum 4 KB in our current demo.
Buffer size: 1 MiB memory area used while reading/writing./* */