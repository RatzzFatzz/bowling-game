package model;

public class TwoRollRoundScore extends RoundScore {
    protected boolean strike = false;
    protected boolean spare = false;

    public TwoRollRoundScore() {
        super(2);
    }

    public void addRoll(int roll) {
        rolls.add(roll);
        if (roll == 10) strike = true;
        else if (getRollSum() == 10) spare = true;
    }

    public int getScore() {
        int result = 0;

        result += getRollSum();
        if (strike) result += getBonusScore(2);
        else if (spare) result += getBonusScore(1);

        return result;
    }

    private int getBonusScore(int depth) {
        return getNext() != null ? getNext().getBonusScoreRecursive(depth) : 0;
    }
}
