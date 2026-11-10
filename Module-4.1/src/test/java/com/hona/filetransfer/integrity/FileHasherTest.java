package com.hona.filetransfer.integrity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileHasherTest {

    @Test
    void shouldCalculateCorrectSha256(@TempDir Path tempDir)
            throws IOException {

        Path file = tempDir.resolve("sample.txt");

        Files.writeString(file, "abc");

        String hash = FileHasher.calculateSha256(file);

        assertEquals(
                "ba7816bf8f01cfea414140de5dae2223"
                        + "b00361a396177a9cb410ff61f20015ad",
                hash
        );
    }

    @Test
    void shouldCalculateHashForEmptyFile(@TempDir Path tempDir)
            throws IOException {

        Path file = tempDir.resolve("empty.bin");

        Files.createFile(file);

        String hash = FileHasher.calculateSha256(file);

        assertEquals(
                "e3b0c44298fc1c149afbf4c8996fb924"
                        + "27ae41e4649b934ca495991b7852b855",
                hash
        );
    }

    @Test
    void shouldProduceSameHashForIdenticalFiles(@TempDir Path tempDir)
            throws IOException {

        Path firstFile = tempDir.resolve("first.bin");
        Path secondFile = tempDir.resolve("second.bin");

        byte[] data = {10, 20, 30, 40, 50};

        Files.write(firstFile, data);
        Files.write(secondFile, data);

        String firstHash = FileHasher.calculateSha256(firstFile);
        String secondHash = FileHasher.calculateSha256(secondFile);

        assertEquals(firstHash, secondHash);
    }
}