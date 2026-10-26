
public class SwitchExpressionDemo {

    public static void main(String[] args) {

        int choice = 2;

        String description = switch (choice) {
            case 1 -> "Count words";
            case 2 -> "Count lines";
            case 3 -> "Count unique words";
            default -> "Invalid choice";
        };

        System.out.println("Selected operation: " + description);
    }
}






