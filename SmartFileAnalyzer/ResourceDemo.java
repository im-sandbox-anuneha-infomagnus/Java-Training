
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResourceDemo {

    public static void main(String[] args) {

        Path input = Path.of("input.txt");

        try (BufferedReader reader = Files.newBufferedReader(input)) {

            String line = reader.readLine();

            System.out.println("First line: " + line);

        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }

        System.out.println("File processing completed.");
    }
}
