package com.hona.filetransfer.merger;

import com.hona.filetransfer.calculator.ChunkCalculator;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class ChunkMerger {

    private static final int BUFFER_SIZE = 1024 * 1024;

    public void merge(
            Path chunksDirectory,
            Path outputFile,
            long fileSize,
            long chunkSize
    ) throws IOException {

        if (fileSize < 0 || chunkSize <= 0) {
            throw new IllegalArgumentException(
                    "Invalid file size or chunk size"
            );
        }

        if (!Files.isDirectory(chunksDirectory)) {
            throw new IOException(
                    "Chunks directory does not exist"
            );
        }

        Path finalFile = outputFile.toAbsolutePath();
        Path outputDirectory = finalFile.getParent();

        Files.createDirectories(outputDirectory);

        if (Files.exists(finalFile)) {
            throw new FileAlreadyExistsException(
                    finalFile.toString()
            );
        }

        Path temporaryFile = outputDirectory.resolve(
                finalFile.getFileName() + ".part"
        );

        long totalChunks =
                ChunkCalculator.calculateChunkCount(
                        fileSize,
                        chunkSize
                );

        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

        boolean temporaryFileCreated = false;
        boolean mergeCompleted = false;

        try {

            try (FileChannel outputChannel = FileChannel.open(
                    temporaryFile,
                    StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.WRITE
            )) {

                temporaryFileCreated = true;

                for (long chunkIndex = 0;
                     chunkIndex < totalChunks;
                     chunkIndex++) {

                    String chunkName =
                            String.format("chunk-%03d.bin", chunkIndex);

                    Path chunkPath =
                            chunksDirectory.resolve(chunkName);

                    long offset =
                            ChunkCalculator.calculateOffset(
                                    chunkIndex,
                                    chunkSize
                            );

                    long expectedLength =
                            ChunkCalculator.calculateChunkLength(
                                    fileSize,
                                    offset,
                                    chunkSize
                            );

                    if (!Files.isRegularFile(chunkPath)) {
                        throw new IOException(
                                "Missing chunk: " + chunkName
                        );
                    }

                    try (FileChannel inputChannel = FileChannel.open(
                            chunkPath,
                            StandardOpenOption.READ
                    )) {

                        if (inputChannel.size() != expectedLength) {
                            throw new IOException(
                                    "Invalid chunk size: " + chunkName
                            );
                        }

                        long remaining = expectedLength;

                        while (remaining > 0) {

                            buffer.clear();

                            int bytesToRead = (int) Math.min(
                                    buffer.capacity(),
                                    remaining
                            );

                            buffer.limit(bytesToRead);

                            int bytesRead = inputChannel.read(buffer);

                            if (bytesRead == -1) {
                                throw new EOFException(
                                        "Chunk ended unexpectedly: "
                                                + chunkName
                                );
                            }

                            if (bytesRead == 0) {
                                throw new IOException(
                                        "Chunk read made no progress"
                                );
                            }

                            buffer.flip();

                            while (buffer.hasRemaining()) {

                                int bytesWritten =
                                        outputChannel.write(buffer);

                                if (bytesWritten == 0) {
                                    throw new IOException(
                                            "Merge write made no progress"
                                    );
                                }
                            }

                            remaining -= bytesRead;
                        }
                    }
                }
            }

            if (Files.size(temporaryFile) != fileSize) {
                throw new IOException(
                        "Merged file size does not match original size"
                );
            }

            Files.move(temporaryFile, finalFile);

            mergeCompleted = true;

        } finally {

            if (temporaryFileCreated && !mergeCompleted) {
                Files.deleteIfExists(temporaryFile);
            }
        }
    }
}