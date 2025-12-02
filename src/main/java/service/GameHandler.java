package service;

import model.RoundScore;
import model.ThreeRollRoundScore;
import model.TwoRollRoundScore;

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
        if (roll > 10) return false;

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
}
