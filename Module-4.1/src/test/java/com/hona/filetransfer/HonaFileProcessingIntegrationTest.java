package com.hona.filetransfer;

import com.hona.filetransfer.integrity.FileHasher;
import com.hona.filetransfer.merger.ChunkMerger;
import com.hona.filetransfer.splitter.ChunkSplitter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HonaFileProcessingIntegrationTest {

    @Test
    void shouldSplitMergeAndVerifyOriginalFile(@TempDir Path tempDir)
            throws IOException {

        Path sourceFile = tempDir.resolve("original.bin");
        Path chunksDirectory = tempDir.resolve("chunks");
        Path mergedFile = tempDir.resolve("reconstructed.bin");

        byte[] originalData = new byte[10 * 1024];

        for (int index = 0; index < originalData.length; index++) {
            originalData[index] = (byte) (index % 256);
        }

        Files.write(sourceFile, originalData);

        long chunkSize = 4 * 1024;

        ChunkSplitter splitter = new ChunkSplitter();

        long totalChunks = splitter.split(
                sourceFile,
                chunksDirectory,
                chunkSize
        );

        assertEquals(3, totalChunks);

        ChunkMerger merger = new ChunkMerger();

        merger.merge(
                chunksDirectory,
                mergedFile,
                Files.size(sourceFile),
                chunkSize
        );

        assertEquals(
                Files.size(sourceFile),
                Files.size(mergedFile)
        );

        String originalHash =
                FileHasher.calculateSha256(sourceFile);

        String mergedHash =
                FileHasher.calculateSha256(mergedFile);

        assertNotNull(originalHash);
        assertEquals(originalHash, mergedHash);
    }
}