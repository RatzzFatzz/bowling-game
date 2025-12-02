package model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class RoundScore {
    @Setter
    private RoundScore next;

    protected final int[] rolls;
    protected int rollCount = 0;
    protected boolean strike = false;
    protected boolean spare = false;

    public RoundScore(int maxRolls) {
        rolls = new int[maxRolls];
    }

    public void addRoll(int roll) {
        rolls[rollCount++] = roll;
        if (roll == 10) strike = true;
        else if (rolls.length == 2 && getRollSum() == 10) spare = true;
    }

    public int getGameScore() {
        if (next == null) {
            return getScore();
        }
        return getScore() + next.getGameScore();
    }

    public int getScore() {
        int result = 0;

        result += getRollSum();
        if (strike) result += getBonusScore(2);
        else if (spare) result += getBonusScore(1);

        return result;
    }

    private int getRollSum() {
        int rollSum = 0;
        for (int roll : rolls) {
            rollSum += roll;
        }
        return rollSum;
    }

    private int getBonusScore(int depth) {
        return getNext() != null ? getNext().getBonusScoreRecursive(depth) : 0;
    }

    private int getBonusScoreRecursive(int depth) {
        int result = 0;

        if (rollCount >= depth) {
            for (int i = 0; i < depth; i++) {
                result += rolls[i];
            }
        } else if (rollCount == 1) {
            result += rolls[0];
            if (getNext() != null) result += getNext().getBonusScoreRecursive(depth - 1);
        }

        return result;
    }

    public boolean hasNext() {
        return next != null;
    }
}
