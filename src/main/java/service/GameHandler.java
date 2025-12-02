package service;

import model.RoundScore;
import model.ThreeRollRoundScore;
import model.TwoRollRoundScore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameHandler {
    private final RoundScore first = new TwoRollRoundScore();
    private RoundScore current = first;
    private int round = 1;
    private int currentScore = 0;
    private int rollCount = 0;

    /**
     * @return current game score
     */
    public int getGameScore() {
        int result = 0;

        RoundScore it = first;
        do {
            result += it.getScore();
            it = it.getNext();
        } while (it != null);

        return result;
    }

    /**
     * Add a roll to the current round
     * @param roll amount of tipped over pins
     * @return true if roll is valid, else false
     */
    public boolean addRoll(int roll) {
        if (roll > 10 || roll < 0) return false;

        if (current instanceof TwoRollRoundScore) {
            if (currentScore + roll > 10) return false;
            currentScore += roll;
            rollCount++;
            current.addRoll(roll);

            if (rollCount == 2 || currentScore == 10) nextRound();
        } else if (current instanceof ThreeRollRoundScore) {
            if (rollCount == 1 && currentScore < 10 && currentScore + roll > 10) return false; // second roll too high
            if (rollCount == 2 && currentScore < 10) return false; // game already complete

            currentScore += roll;
            rollCount++;
            current.addRoll(roll);
        }

        return true;
    }

    private void nextRound() {
        round++;
        currentScore = 0;
        rollCount = 0;
        current.setNext(round == 10 ? new ThreeRollRoundScore() : new TwoRollRoundScore());
        current = current.getNext();
    }

    public boolean isGameComplete() {
        return round == 10 && ((rollCount == 2 && currentScore < 10) || rollCount == 3);
    }

    public void printScore() {

        String scoreTable = """
            +---------------------------------------------------------------------------------------------+
            |  FRAME |   1   |   2   |   3   |   4   |   5   |   6   |   7   |   8   |   9   |     10     |
            +--------+-------+-------+-------+-------+-------+-------+-------+-------+-------+------------+
            | ROLLS  | [%s][%s]| [%s][%s]| [%s][%s]| [%s][%s]| [%s][%s]| [%s][%s]| [%s][%s]| [%s][%s]| [%s][%s]| [%s][%s][%s]  |
            +--------+-------+-------+-------+-------+-------+-------+-------+-------+-------+------------+
            | SCORE  |  %3s  |  %3s  |  %3s  |  %3s  |  %3s  |  %3s  |  %3s  |  %3s  |  %3s  |    %3s     |
            +--------+-------+-------+-------+-------+-------+-------+-------+-------+-------+------------+
            """;

        String[] rolls = new String[21];
        Integer[] scores = new Integer[10];
        Arrays.fill(rolls, " ");
        Arrays.fill(scores, null);

        RoundScore it = first;
        int frame = 0;
        do {
            if (frame < 9 && it.getRolls().size() == 2 || frame == 9 && it.getRolls().size() == 3) {
                addTo(rolls, frame * 2, it.getRolls(), null);
            } else {
                addTo(rolls, frame * 2, it.getRolls(), " ");
            }

            if (frame > 0) scores[frame] = scores[frame - 1] + it.getScore();
            else scores[frame] = it.getScore();

            it = it.getNext();
            frame++;
        } while (it != null);

        List<String> values = new ArrayList<>(List.of(rolls));
        List<String> convertedScores = Arrays.stream(scores).map(score -> score == null ? " " : String.valueOf(score)).toList();
        values.addAll(convertedScores);

        scoreTable = scoreTable.formatted(values.toArray());
        System.out.println(scoreTable);
    }

    private void addTo(String[] rolls, int index, List<Integer> rollsInt, String placeholder) {
        for (int i = 0; i < rollsInt.size(); i++) {
            rolls[i + index] = rollsInt.get(i) == 10 ? "X" : String.valueOf(rollsInt.get(i));
        }
        if (placeholder != null) rolls[index + rollsInt.size()] = placeholder;
    }
}
