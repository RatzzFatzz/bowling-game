import service.GameHandler;
import service.PrintService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GameHandler gameHandler = new GameHandler();

        System.out.print("Enter your name: ");
        Scanner scanner = new Scanner(System.in);
        System.out.printf("Hello %s. Let's play some bowling, shall we?\n", scanner.nextLine());

        while (!gameHandler.isGameComplete()) {
            System.out.print("How many pins did you tip over? ");
            if (!scanner.hasNextInt()) {
                System.out.println("Only numbers between 0 and 10 are allowed");
                scanner.next();
                continue;
            }
            if (!gameHandler.addRoll(scanner.nextInt())) {
                System.out.println("I see what you are trying. You can't cheat me. How many pins did you actually hit?");
                continue;
            }
            PrintService.printScoreboard(gameHandler.getFirst());
        }

        System.out.println("Final game score: " + gameHandler.getGameScore());
    }
}
