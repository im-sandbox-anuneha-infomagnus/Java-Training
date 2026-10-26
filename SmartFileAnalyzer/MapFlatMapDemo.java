
import java.util.stream.Stream;

public class MapFlatMapDemo {

    public static void main(String[] args) {

        var lines = Stream.of(
                "Java is easy",
                "Streams are useful"
        );

        System.out.println("Using map():");

        Stream.of("Java is easy", "Streams are useful")
                .map(String::toUpperCase)
                .forEach(System.out::println);

        System.out.println("\nUsing flatMap():");

        Stream.of("Java is easy", "Streams are useful")
                .flatMap(line -> Stream.of(line.split(" ")))
                .forEach(System.out::println);
    }
}
