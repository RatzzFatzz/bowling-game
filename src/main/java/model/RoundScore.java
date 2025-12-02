package model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public abstract class RoundScore {
    @Getter
    @Setter
    private RoundScore next;

    @Getter
    protected final List<Integer> rolls;

    public RoundScore(int maxRolls) {
        rolls = new ArrayList<>(maxRolls);
    }

    public abstract void addRoll(int roll);

    public abstract int getScore();

    protected int getRollSum() {
        return rolls.stream().mapToInt(Integer::intValue).sum();
    }

    protected int getBonusScoreRecursive(int depth) {
        int result = 0;

        if (rolls.size() >= depth) {
            for (int i = 0; i < depth; i++) {
                result += rolls.get(i);
            }
        } else if (rolls.size() == 1) {
            result += rolls.getFirst();
            if (getNext() != null) result += getNext().getBonusScoreRecursive(depth - 1);
        }

        return result;
    }
}
