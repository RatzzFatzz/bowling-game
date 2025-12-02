public class ScoreHandler {
    private final RoundScore first = new RoundScore();
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
            it = it.next;
        } while (it != null);

        return result;
    }

    /**
     * Add a roll to the current round
     * @param roll amount of tipped over pins
     * @return true if action is valid, else false
     */
    public boolean addRoll(int roll) {
        if (currentScore + roll > 10) return false;
        currentScore += roll;
        rollCount++;
        current.addRoll(roll);

        if (rollCount == 2 || currentScore == 10) {
            round++;
            currentScore = 0;
            rollCount = 0;
            current.setNext(round == 10 ? new FinalRoundScore() : new RoundScore());
            current = current.getNext();
        }
        return true;
    }
}
