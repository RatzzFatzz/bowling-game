package service;

import model.RoundScore;

public class PrintService {
    private static final String THREE_ROLL_PLACEHOLDER = "  [%s][%s][%s] |";
    private static final String TWO_ROLL_PLACEHOLDER = " [%s][%s]|";

    public static void printScoreboard(RoundScore first) {
        String topLine = "+---------------------------------------------------------------------------------------------+";
        String separatorLine = "+--------+-------+-------+-------+-------+-------+-------+-------+-------+-------+------------+";
        String frameLine = "| FRAME  |   1   |   2   |   3   |   4   |   5   |   6   |   7   |   8   |   9   |     10     |";
        StringBuilder rollsLine = new StringBuilder().append("| ROLLS  |");
        StringBuilder scoreLine = new StringBuilder().append("| SCORE  |");

        RoundScore it = first;
        int frame = 1;
        int score = first.getScore();
        scoreLine.append(scoreBlock(score, frame));
        rollsLine.append(rollBlock(it, frame));
        frame++;
        while (it.hasNext()) {
            it = it.getNext();
            if (it.getRollCount() == 0) break;
            score += it.getScore();
            scoreLine.append(scoreBlock(score, frame));
            rollsLine.append(rollBlock(it, frame));
            frame++;
        }

        if (frame <= 10) {
            for (int i = frame; i < 10; i++) {
                scoreLine.append("       |");
                rollsLine.append(String.format(TWO_ROLL_PLACEHOLDER, " ", " "));
            }
            scoreLine.append("            |");
            rollsLine.append(String.format(THREE_ROLL_PLACEHOLDER, " ", " ", " "));
        }

        System.out.println(topLine);
        System.out.println(frameLine);
        System.out.println(separatorLine);
        System.out.println(rollsLine);
        System.out.println(separatorLine);
        System.out.println(scoreLine);
        System.out.println(separatorLine);

    }

    private static String scoreBlock(int score, int frame) {
        return String.format(frame == 10 ? "    %3s     |" :"  %3s  |", score);
    }

    private static String rollBlock(RoundScore score, int frame) {
        int[] rolls = score.getRolls();

        if (frame == 10) {
            if (score.getRollCount() == 1) {
                return String.format(THREE_ROLL_PLACEHOLDER,  rolls[0] == 10 ? "X" : rolls[0], " ", " ");
            }
            if (score.getRollCount() == 2) {
                if (score.getScore() == 10) return String.format(THREE_ROLL_PLACEHOLDER, score.getRolls()[0], "/", " ");
                return String.format(THREE_ROLL_PLACEHOLDER,  rolls[0] == 10 ? "X" : rolls[0], rolls[1] == 10 ? "X" : rolls[1], " ");
            }
            if (score.getScore() < 20 && rolls[0] != 10) return String.format(THREE_ROLL_PLACEHOLDER, score.getRolls()[0], "/", score.getRolls()[2]);
            if (rolls[0] + rolls[1] == 10) return String.format(THREE_ROLL_PLACEHOLDER, score.getRolls()[0], "/", "X");
            if (rolls[1] + rolls[2] == 10) return String.format(THREE_ROLL_PLACEHOLDER, "X", score.getRolls()[1], "/");
            return String.format(THREE_ROLL_PLACEHOLDER,  rolls[0] == 10 ? "X" : rolls[0], rolls[1] == 10 ? "X" : rolls[1], rolls[2] == 10 ? "X" : rolls[2]);
        }

        if (score.isStrike()) return String.format(TWO_ROLL_PLACEHOLDER, " ", "X");
        if(score.isSpare()) return String.format(TWO_ROLL_PLACEHOLDER, score.getRolls()[0], "/");
        if (score.getRollCount() == 1) return String.format(TWO_ROLL_PLACEHOLDER, score.getRolls()[0], " ");
        return String.format(TWO_ROLL_PLACEHOLDER, score.getRolls()[0], score.getRolls()[1]);
    }
}
