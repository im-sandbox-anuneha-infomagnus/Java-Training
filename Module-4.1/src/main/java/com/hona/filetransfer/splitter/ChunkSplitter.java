
package com.hona.filetransfer.splitter;

import com.hona.filetransfer.calculator.ChunkCalculator;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;

public final class ChunkSplitter {

    private static final int BUFFER_SIZE = 1024 * 1024;

    public long split(
            Path sourceFile,
            Path chunksDirectory,
            long chunkSize
    ) throws IOException {

        if (chunkSize <= 0) {
            throw new IllegalArgumentException(
                    "Chunk size must be positive"
            );
        }

        if (!Files.isRegularFile(sourceFile)) {
            throw new IOException(
                    "Source must be an existing regular file"
            );
        }

        BasicFileAttributes initialAttributes =
                Files.readAttributes(
                        sourceFile,
                        BasicFileAttributes.class
                );

        Files.createDirectories(chunksDirectory);

        try (FileChannel sourceChannel = FileChannel.open(
                sourceFile,
                StandardOpenOption.READ
        )) {

            long fileSize = sourceChannel.size();

            if (fileSize != initialAttributes.size()) {
                throw new IOException(
                        "Source file changed before splitting started"
                );
            }

            long totalChunks =
                    ChunkCalculator.calculateChunkCount(
                            fileSize,
                            chunkSize
                    );

            if (totalChunks == 0) {

                BasicFileAttributes finalAttributes =
                        Files.readAttributes(
                                sourceFile,
                                BasicFileAttributes.class
                        );

                if (hasSourceChanged(initialAttributes, finalAttributes)) {
                    throw new IOException(
                            "Source file changed during splitting"
                    );
                }

                return 0;
            }

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            long completedChunks = 0;

            try {

                for (long chunkIndex = 0;
                     chunkIndex < totalChunks;
                     chunkIndex++) {

                    long offset =
                            ChunkCalculator.calculateOffset(
                                    chunkIndex,
                                    chunkSize
                            );

                    long chunkLength =
                            ChunkCalculator.calculateChunkLength(
                                    fileSize,
                                    offset,
                                    chunkSize
                            );

                    writeChunk(
                            sourceChannel,
                            chunksDirectory,
                            chunkIndex,
                            offset,
                            chunkLength,
                            buffer
                    );

                    completedChunks++;
                }

                BasicFileAttributes finalAttributes =
                        Files.readAttributes(
                                sourceFile,
                                BasicFileAttributes.class
                        );

                if (hasSourceChanged(initialAttributes, finalAttributes)) {
                    throw new IOException(
                            "Source file changed during splitting"
                    );
                }

                return totalChunks;

            } catch (IOException | RuntimeException failure) {

                deleteCompletedChunks(
                        chunksDirectory,
                        completedChunks,
                        failure
                );

                throw failure;
            }
        }
    }

    private void writeChunk(
            FileChannel sourceChannel,
            Path chunksDirectory,
            long chunkIndex,
            long offset,
            long chunkLength,
            ByteBuffer buffer
    ) throws IOException {

        String chunkName =
                String.format("chunk-%03d.bin", chunkIndex);

        Path chunkPath = chunksDirectory.resolve(chunkName);

        Path temporaryPath =
                chunksDirectory.resolve(chunkName + ".part");

        boolean temporaryFileCreated = false;
        boolean chunkPublished = false;

        try {

            try (FileChannel outputChannel = FileChannel.open(
                    temporaryPath,
                    StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.WRITE
            )) {

                temporaryFileCreated = true;

                long remaining = chunkLength;
                long sourcePosition = offset;

                while (remaining > 0) {

                    buffer.clear();

                    int bytesToRead = (int) Math.min(
                            buffer.capacity(),
                            remaining
                    );

                    buffer.limit(bytesToRead);

                    int bytesRead =
                            sourceChannel.read(buffer, sourcePosition);

                    if (bytesRead == -1) {
                        throw new EOFException(
                                "Source file ended unexpectedly"
                        );
                    }

                    if (bytesRead == 0) {
                        throw new IOException(
                                "Source read made no progress"
                        );
                    }

                    buffer.flip();

                    while (buffer.hasRemaining()) {

                        int bytesWritten =
                                outputChannel.write(buffer);

                        if (bytesWritten == 0) {
                            throw new IOException(
                                    "Chunk write made no progress"
                            );
                        }
                    }

                    sourcePosition += bytesRead;
                    remaining -= bytesRead;
                }
            }

            if (Files.size(temporaryPath) != chunkLength) {
                throw new IOException(
                        "Written chunk size does not match expected size"
                );
            }

            Files.move(temporaryPath, chunkPath);

            chunkPublished = true;

        } finally {

            if (temporaryFileCreated && !chunkPublished) {
                Files.deleteIfExists(temporaryPath);
            }
        }
    }

    private boolean hasSourceChanged(
            BasicFileAttributes initial,
            BasicFileAttributes current
    ) {

        boolean sizeChanged =
                initial.size() != current.size();

        boolean modifiedTimeChanged =
                !initial.lastModifiedTime()
                        .equals(current.lastModifiedTime());

        boolean identityChanged =
                initial.fileKey() != null
                        && current.fileKey() != null
                        && !initial.fileKey().equals(current.fileKey());

        return sizeChanged || modifiedTimeChanged || identityChanged;
    }

    private void deleteCompletedChunks(
            Path chunksDirectory,
            long completedChunks,
            Exception originalFailure
    ) {

        for (long index = 0; index < completedChunks; index++) {

            String chunkName =
                    String.format("chunk-%03d.bin", index);

            Path chunkPath = chunksDirectory.resolve(chunkName);

            try {
                Files.deleteIfExists(chunkPath);

            } catch (IOException cleanupFailure) {
                originalFailure.addSuppressed(cleanupFailure);
            }
        }
    }
}
