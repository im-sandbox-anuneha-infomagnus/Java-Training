package com.hona.filetransfer.splitter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChunkSplitterTest {

    @Test
    void shouldSplitFileIntoNumberedChunks(@TempDir Path tempDir)
            throws IOException {

        Path sourceFile = tempDir.resolve("original.bin");
        Path chunksDirectory = tempDir.resolve("chunks");

        byte[] originalData = {
                10, 20, 30, 40, 50,
                60, 70, 80, 90, 100
        };

        Files.write(sourceFile, originalData);

        ChunkSplitter splitter = new ChunkSplitter();

        long totalChunks = splitter.split(
                sourceFile,
                chunksDirectory,
                4
        );

        assertEquals(3, totalChunks);

        assertArrayEquals(
                new byte[]{10, 20, 30, 40},
                Files.readAllBytes(chunksDirectory.resolve("chunk-000.bin"))
        );

        assertArrayEquals(
                new byte[]{50, 60, 70, 80},
                Files.readAllBytes(chunksDirectory.resolve("chunk-001.bin"))
        );

        assertArrayEquals(
                new byte[]{90, 100},
                Files.readAllBytes(chunksDirectory.resolve("chunk-002.bin"))
        );

        try (Stream<Path> files = Files.list(chunksDirectory)) {
            assertEquals(3, files.count());
        }
    }

    @Test
    void shouldHandleEmptyFile(@TempDir Path tempDir)
            throws IOException {

        Path sourceFile = tempDir.resolve("empty.bin");
        Path chunksDirectory = tempDir.resolve("chunks");

        Files.createFile(sourceFile);

        ChunkSplitter splitter = new ChunkSplitter();

        long totalChunks = splitter.split(
                sourceFile,
                chunksDirectory,
                4
        );

        assertEquals(0, totalChunks);

        try (Stream<Path> files = Files.list(chunksDirectory)) {
            assertEquals(0, files.count());
        }
    }

    @Test
    void shouldRejectInvalidChunkSize(@TempDir Path tempDir)
            throws IOException {

        Path sourceFile = tempDir.resolve("original.bin");
        Path chunksDirectory = tempDir.resolve("chunks");

        Files.write(sourceFile, new byte[]{10, 20, 30});

        ChunkSplitter splitter = new ChunkSplitter();

        assertThrows(
                IllegalArgumentException.class,
                () -> splitter.split(sourceFile, chunksDirectory, 0)
        );
    }

    @Test
    void shouldNotOverwriteExistingChunk(@TempDir Path tempDir)
            throws IOException {

        Path sourceFile = tempDir.resolve("original.bin");
        Path chunksDirectory = tempDir.resolve("chunks");

        Files.write(sourceFile, new byte[]{10, 20, 30, 40});

        Files.createDirectories(chunksDirectory);

        Path existingChunk = chunksDirectory.resolve("chunk-000.bin");

        Files.write(existingChunk, new byte[]{99});

        ChunkSplitter splitter = new ChunkSplitter();

        assertThrows(
                IOException.class,
                () -> splitter.split(sourceFile, chunksDirectory, 4)
        );

        assertArrayEquals(
                new byte[]{99},
                Files.readAllBytes(existingChunk)
        );
    }
}