package com.hona.filetransfer.calculator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChunkCalculatorTest {

    @Test
    void shouldCalculateChunkCountForExactDivision() {
        assertEquals(4L, ChunkCalculator.calculateChunkCount(120, 30));
    }

    @Test
    void shouldCalculateChunkCountForPartialChunk() {
        assertEquals(4L, ChunkCalculator.calculateChunkCount(100, 30));
    }

    @Test
    void shouldHandleFileSmallerThanChunkSize() {
        assertEquals(1L, ChunkCalculator.calculateChunkCount(20, 30));
    }

    @Test
    void shouldHandleEmptyFile() {
        assertEquals(0L, ChunkCalculator.calculateChunkCount(0, 30));
    }

    @Test
    void shouldRejectNegativeFileSize() {
        assertThrows(IllegalArgumentException.class,
                () -> ChunkCalculator.calculateChunkCount(-10, 30));
    }

    @Test
    void shouldRejectZeroChunkSize() {
        assertThrows(IllegalArgumentException.class,
                () -> ChunkCalculator.calculateChunkCount(100, 0));
    }

    @Test
    void shouldCalculateChunkOffset() {
        assertEquals(90L, ChunkCalculator.calculateOffset(3, 30));
    }

    @Test
    void shouldDetectOffsetOverflow() {
        assertThrows(ArithmeticException.class,
                () -> ChunkCalculator.calculateOffset(Long.MAX_VALUE, 2));
    }

    @Test
    void shouldCalculateFullChunkLength() {
        assertEquals(30L, ChunkCalculator.calculateChunkLength(100, 60, 30));
    }

    @Test
    void shouldCalculateLastPartialChunkLength() {
        assertEquals(10L, ChunkCalculator.calculateChunkLength(100, 90, 30));
    }

    @Test
    void shouldRejectOffsetAtEndOfFile() {
        assertThrows(IllegalArgumentException.class,
                () -> ChunkCalculator.calculateChunkLength(100, 100, 30));
    }
    @Test
    void shouldRejectOffsetNotAlignedWithChunkSize() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ChunkCalculator.calculateChunkLength(100, 45, 30)
        );
    }


}
