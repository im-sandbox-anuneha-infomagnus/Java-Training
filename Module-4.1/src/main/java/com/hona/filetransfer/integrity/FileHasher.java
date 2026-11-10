package com.hona.filetransfer.integrity;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public final class FileHasher {

    private static final int BUFFER_SIZE = 1024 * 1024;

    private FileHasher() {
    }

    public static String calculateSha256(Path file)
            throws IOException {

        MessageDigest digest;

        try {
            digest = MessageDigest.getInstance("SHA-256");

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "SHA-256 algorithm is unavailable", e
            );
        }

        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

        try (FileChannel channel = FileChannel.open(
                file,
                StandardOpenOption.READ
        )) {

            while (true) {

                buffer.clear();

                int bytesRead = channel.read(buffer);

                if (bytesRead == -1) {
                    break;
                }

                if (bytesRead == 0) {
                    throw new IOException(
                            "File reading made no progress"
                    );
                }

                buffer.flip();

                digest.update(buffer);
            }
        }

        return HexFormat.of().formatHex(digest.digest());
    }
}