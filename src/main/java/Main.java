import service.GameHandler;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GameHandler gameHandler = new GameHandler();

        System.out.print("Enter your name: ");
        Scanner scanner = new Scanner(System.in);
        System.out.printf("Hello %s. Let's play some bowling, shall we?\n", scanner.nextLine());

        while (!gameHandler.isGameComplete()) {
            System.out.print("How many pins did you tip over? ");
            int pins = scanner.nextInt();
            if (pins > 10 || pins < 0 || !gameHandler.addRoll(pins)) {
                System.out.println("I see what you are trying. You can't cheat me. How many pins did you actually hit?");
            }
        }

        System.out.println("Gamescore: " + gameHandler.getGameScore());
    }
}
