import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class RoundScore {
    @Getter
    @Setter
    protected RoundScore next;

    protected final List<Integer> rolls = new ArrayList<>(3);
    protected boolean strike = false;
    protected boolean spare = false;

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

    protected int getRollSum() {
        return rolls.stream().mapToInt(Integer::intValue).sum();
    }

    protected int getBonusScore(int depth) {
        return next != null ? next.getBonusScoreRecursive(depth) : 0;
    }

    protected int getBonusScoreRecursive(int depth) {
        int result = 0;

        if (rolls.size() >= depth) {
            for (int i = 0; i < depth; i++) {
                result += rolls.get(i);
            }
        } else if (rolls.size() == 1) {
            result += rolls.getFirst();
            if (next != null) result += next.getBonusScoreRecursive(depth - 1);
        }

        return result;
    }
}
