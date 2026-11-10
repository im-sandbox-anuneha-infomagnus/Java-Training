package com.hona.filetransfer.merger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChunkMergerTest {

    @Test
    void shouldMergeChunksInCorrectOrder(@TempDir Path tempDir)
            throws IOException {

        Path chunksDirectory = tempDir.resolve("chunks");
        Path outputFile = tempDir.resolve("reconstructed.bin");

        Files.createDirectories(chunksDirectory);

        Files.write(
                chunksDirectory.resolve("chunk-000.bin"),
                new byte[]{10, 20, 30, 40}
        );

        Files.write(
                chunksDirectory.resolve("chunk-001.bin"),
                new byte[]{50, 60, 70, 80}
        );

        Files.write(
                chunksDirectory.resolve("chunk-002.bin"),
                new byte[]{90, 100}
        );

        ChunkMerger merger = new ChunkMerger();

        merger.merge(chunksDirectory, outputFile, 10, 4);

        byte[] expectedData = {
                10, 20, 30, 40, 50,
                60, 70, 80, 90, 100
        };

        assertArrayEquals(
                expectedData,
                Files.readAllBytes(outputFile)
        );

        assertEquals(10, Files.size(outputFile));
    }

    @Test
    void shouldRejectMissingChunk(@TempDir Path tempDir)
            throws IOException {

        Path chunksDirectory = tempDir.resolve("chunks");
        Path outputFile = tempDir.resolve("reconstructed.bin");

        Files.createDirectories(chunksDirectory);

        Files.write(
                chunksDirectory.resolve("chunk-000.bin"),
                new byte[]{10, 20, 30, 40}
        );

        ChunkMerger merger = new ChunkMerger();

        assertThrows(
                IOException.class,
                () -> merger.merge(chunksDirectory, outputFile, 8, 4)
        );

        assertFalse(Files.exists(outputFile));
        assertFalse(Files.exists(tempDir.resolve("reconstructed.bin.part")));
    }

    @Test
    void shouldRejectIncorrectChunkSize(@TempDir Path tempDir)
            throws IOException {

        Path chunksDirectory = tempDir.resolve("chunks");
        Path outputFile = tempDir.resolve("reconstructed.bin");

        Files.createDirectories(chunksDirectory);

        Files.write(
                chunksDirectory.resolve("chunk-000.bin"),
                new byte[]{10, 20, 30}
        );

        ChunkMerger merger = new ChunkMerger();

        assertThrows(
                IOException.class,
                () -> merger.merge(chunksDirectory, outputFile, 4, 4)
        );

        assertFalse(Files.exists(outputFile));
    }

    @Test
    void shouldMergeEmptyFile(@TempDir Path tempDir)
            throws IOException {

        Path chunksDirectory = tempDir.resolve("chunks");
        Path outputFile = tempDir.resolve("reconstructed.bin");

        Files.createDirectories(chunksDirectory);

        ChunkMerger merger = new ChunkMerger();

        merger.merge(chunksDirectory, outputFile, 0, 4);

        assertEquals(0, Files.size(outputFile));
    }

    @Test
    void shouldNotOverwriteExistingOutput(@TempDir Path tempDir)
            throws IOException {

        Path chunksDirectory = tempDir.resolve("chunks");
        Path outputFile = tempDir.resolve("reconstructed.bin");

        Files.createDirectories(chunksDirectory);

        byte[] existingData = {99, 98, 97};

        Files.write(outputFile, existingData);

        ChunkMerger merger = new ChunkMerger();

        assertThrows(
                IOException.class,
                () -> merger.merge(chunksDirectory, outputFile, 4, 4)
        );

        assertArrayEquals(
                existingData,
                Files.readAllBytes(outputFile)
        );
    }
}