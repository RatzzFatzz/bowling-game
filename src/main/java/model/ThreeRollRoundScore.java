package model;

public class ThreeRollRoundScore extends RoundScore {
    public ThreeRollRoundScore() {
        super(3);
    }

    public void addRoll(int roll) {
        rolls.add(roll);
    }

    public int getScore() {
        return getRollSum();
    }
}
