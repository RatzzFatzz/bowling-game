package service;

import lombok.Getter;
import model.RoundScore;

public class GameHandler {
    @Getter
    private final RoundScore first = new RoundScore(2);
    private RoundScore current = first;
    private int round = 1;
    private int currentScore = 0;

    /**
     * @return current game score
     */
    public int getGameScore() {
        return first.getGameScore();
    }

    /**
     * Add a roll to the current round
     * @param roll amount of tipped over pins
     * @return true if roll is valid, else false
     */
    public boolean addRoll(int roll) {
        if (roll > 10 || roll < 0) return false;

        if (round <= 9) {
            if (currentScore + roll > 10) return false;
            currentScore += roll;
            current.addRoll(roll);

            if (current.getRollCount() == 2 || currentScore == 10) nextRound();
        } else if (round == 10) {
            if (current.getRollCount() == 1 && currentScore < 10 && currentScore + roll > 10) return false; // second roll too high
            if (current.getRollCount() == 2 && currentScore < 10) return false; // game already complete

            currentScore += roll;
            current.addRoll(roll);
        }

        return true;
    }

    private void nextRound() {
        round++;
        currentScore = 0;
        current.setNext(round == 10 ? new RoundScore(3) : new RoundScore(2));
        current = current.getNext();
    }

    public boolean isGameComplete() {
        return round == 10 && ((current.getRollCount() == 2 && currentScore < 10) || current.getRollCount() == 3);
    }
}
