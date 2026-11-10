package com.hona.filetransfer.calculator;

public final class ChunkCalculator {

    private ChunkCalculator() {
        // Utility class: no objects need to be created.
    }

    public static long calculateChunkCount(
            long fileSize,
            long chunkSize) {

        if (fileSize < 0) {
            throw new IllegalArgumentException(
                    "File size cannot be negative"
            );
        }

        if (chunkSize <= 0) {
            throw new IllegalArgumentException(
                    "Chunk size must be positive"
            );
        }

        long fullChunks = fileSize / chunkSize;
        long remainingBytes = fileSize % chunkSize;

        return fullChunks + (remainingBytes == 0 ? 0 : 1);
    }

    public static long calculateOffset(
            long chunkIndex,
            long chunkSize) {

        if (chunkIndex < 0) {
            throw new IllegalArgumentException(
                    "Chunk index cannot be negative"
            );
        }

        if (chunkSize <= 0) {
            throw new IllegalArgumentException(
                    "Chunk size must be positive"
            );
        }

        return Math.multiplyExact(chunkIndex, chunkSize);
    }

    public static long calculateChunkLength(
            long fileSize,
            long offset,
            long chunkSize) {

        if (fileSize < 0 || offset < 0 || chunkSize <= 0) {
            throw new IllegalArgumentException(
                    "Invalid file size, offset or chunk size"
            );
        }
        if (offset % chunkSize != 0) {
            throw new IllegalArgumentException(
                    "Offset must be aligned with chunk size"
            );
        }

        if (offset >= fileSize) {
            throw new IllegalArgumentException(
                    "Offset must point inside the file"
            );
        }

        return Math.min(chunkSize, fileSize - offset);
    }
}
