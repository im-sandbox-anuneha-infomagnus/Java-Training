
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Main {

    private static final Pattern WORD_SEPARATOR =
            Pattern.compile("[^\\p{L}\\p{N}']+");

    record FileReport(
            long totalLines,
            long totalWords,
            long uniqueWords,
            long blankLines,
            long totalCharacters,
            int longestLine,
            double processingSeconds
    ) {}

    static class FileStats {
        long lines;
        long words;
        long blankLines;
        long characters;
        int longestLine;

        Set<String> uniqueWords = new HashSet<>();

        void processLine(String line) {
            lines++;
            characters += line.length();
            longestLine = Math.max(longestLine, line.length());

            if (line.isBlank()) {
                blankLines++;
            }

            WORD_SEPARATOR
                    .splitAsStream(line.toLowerCase(Locale.ROOT))
                    .filter(word -> !word.isBlank())
                    .forEach(word -> {
                        words++;
                        uniqueWords.add(word);
                    });
        }

        FileReport createReport(double seconds) {
            return new FileReport(
                    lines,
                    words,
                    uniqueWords.size(),
                    blankLines,
                    characters,
                    longestLine,
                    seconds
            );
        }
    }

    public static void main(String[] args) {
        var input = Path.of("input.txt");
        var output = Path.of("report.txt");
        var stats = new FileStats();

        long start = System.nanoTime();

        try (Stream<String> lines =
                     Files.lines(input, StandardCharsets.UTF_8)) {

            lines.forEach(stats::processLine);

            double seconds =
                    (System.nanoTime() - start) / 1_000_000_000.0;

            var report = stats.createReport(seconds);

            var reportText = """
                    SMART FILE ANALYSIS REPORT
                    ==========================
                    Total lines: %d
                    Total words: %d
                    Unique words: %d
                    Blank lines: %d
                    Total characters: %d
                    Longest line: %d characters
                    Processing time: %.6f seconds
                    """.formatted(
                    report.totalLines(),
                    report.totalWords(),
                    report.uniqueWords(),
                    report.blankLines(),
                    report.totalCharacters(),
                    report.longestLine(),
                    report.processingSeconds()
            );

            Files.writeString(
                    output,
                    reportText,
                    StandardCharsets.UTF_8
            );

            System.out.println(reportText);
            System.out.println(
                    "Report saved to: " + output.toAbsolutePath()
            );

        } catch (IOException e) {
            System.err.println(
                    "File processing failed: " + e.getMessage()
            );
        }
    }
}
