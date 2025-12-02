import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScoreHandlerTest {

    private static Stream<Arguments> rolls() {
        return Stream.of(
                // two rolls without spares or strikes
                Arguments.of(List.of(a(1, 1), a(1, 1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1)), 20),
                // one spare or strike
                Arguments.of(List.of(a(1, 9), a(1, 1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1)), 29),
                Arguments.of(List.of(a(10), a(1, 1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1)), 30),
                // one strike and spare
                Arguments.of(List.of(a(10), a(1, 1), a(1,9), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1)), 39),
                // spare after strike
                Arguments.of(List.of(a(10), a(1, 9), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1)), 47),
                // strike after strike
                Arguments.of(List.of(a(10), a(10), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1)), 49),
                // three striker in a row
                Arguments.of(List.of(a(10), a(10), a(10), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1)), 77),
                // spare in last round
                Arguments.of(List.of(a(1, 1), a(1, 1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1, 9, 1)), 29),
                // spare and strike in last round
                Arguments.of(List.of(a(1, 1), a(1, 1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1, 9, 10)), 38),
                // triple strike last round
                Arguments.of(List.of(a(1, 1), a(1, 1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(10, 10, 10)), 48)
        );
    }

    @ParameterizedTest
    @MethodSource("rolls")
    void getGameScore(List<int[]> input, int expectedScore) {
        ScoreHandler scoreHandler = new ScoreHandler();
        for (int[] round: input) {
            for (int roll: round) {
                boolean valid = scoreHandler.addRoll(roll);
                assertTrue(valid, "Adding roll was invalid");
            }
        }
        int actualScore = scoreHandler.getGameScore();
        assertEquals(expectedScore, actualScore);
    }

    private static int[] a(int... vals) {
        return vals;
    }
}
