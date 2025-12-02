package service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class GameHandlerTest {

    private static Stream<Arguments> fullLengthGames() {
        return Stream.of(
                // two rolls without spares or strikes
                Arguments.of(List.of(a(1, 1), a(1, 1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1)), 20, true),
                // one spare or strike
                Arguments.of(List.of(a(1, 9), a(1, 1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1)), 29, true),
                Arguments.of(List.of(a(10), a(1, 1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1)), 30, true),
                // one strike and spare
                Arguments.of(List.of(a(10), a(1, 1), a(1,9), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1)), 39, true),
                // spare after strike
                Arguments.of(List.of(a(10), a(1, 9), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1)), 47, true),
                // strike after strike
                Arguments.of(List.of(a(10), a(10), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1)), 49, true),
                // three striker in a row
                Arguments.of(List.of(a(10), a(10), a(10), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1)), 77, true),
                // spare in last round
                Arguments.of(List.of(a(1, 1), a(1, 1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1, 9, 1)), 29, true),
                // spare and strike in last round
                Arguments.of(List.of(a(1, 1), a(1, 1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1, 9, 10)), 38, true),
                Arguments.of(List.of(a(1, 1), a(1, 1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(10, 1, 9)), 38, true),
                // triple strike last round
                Arguments.of(List.of(a(1, 1), a(1, 1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(10, 10, 10)), 48, true)
        );
    }

    private static Stream<Arguments> partialLengthGames() {
        return Stream.of(
                // two rolls without spares or strikes
                Arguments.of(List.of(a(1, 1)), 2, false),
                Arguments.of(List.of(a(1, 1), a(1, 1)), 4, false),
                // one spare or strike without next round
                Arguments.of(List.of(a(1, 9)), 10, false),
                Arguments.of(List.of(a(10)), 10, false),
                // one spare or strike with following roll
                Arguments.of(List.of(a(1, 9), a(3)), 16, false),
                Arguments.of(List.of(a(10), a(3)), 16, false),
                // one strike with two following rolls
                Arguments.of(List.of(a(10), a(3, 6)), 28, false),
                // one strike with following spare
                Arguments.of(List.of(a(10), a(3, 7)), 30, false),
                // one strike with following spare and single roll
                Arguments.of(List.of(a(10), a(3, 7), a(1)), 32, false),
                // spare in last round with missing last roll
                Arguments.of(List.of(a(1, 1), a(1, 1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1, 9)), 28, false),
                // strike in last round with missing last rolls
                Arguments.of(List.of(a(1, 1), a(1, 1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(10)), 28, false),
                Arguments.of(List.of(a(1, 1), a(1, 1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(1,1), a(10, 1)), 29, false)
        );
    }

    @ParameterizedTest
    @MethodSource("fullLengthGames")
    @MethodSource("partialLengthGames")
    void getGameScore(List<int[]> input, int expectedScore, boolean expectedGameIsFinished) {
        GameHandler gameHandler = new GameHandler();
        for (int[] round: input) {
            for (int roll: round) {
                boolean valid = gameHandler.addRoll(roll);
                assertTrue(valid, "Adding roll was invalid");
            }
        }
        int actualScore = gameHandler.getGameScore();
        assertEquals(expectedScore, actualScore);
        assertEquals(expectedGameIsFinished, gameHandler.isGameComplete());
    }

    private static Stream<Arguments> rollValues() {
        return Stream.of(
                Arguments.of(a(-1), false),
                Arguments.of(a(-2), false),
                Arguments.of(a(11), false),
                Arguments.of(a(100), false),
                Arguments.of(a(5, 6), false),
                Arguments.of(a(9, 2), false),
                Arguments.of(a(9, 10), false),
                Arguments.of(a(10, 10, 10, 10, 10, 10, 10, 10, 10, 6, 6), false),
                Arguments.of(a(10, 10, 10, 10, 10, 10, 10, 10, 10, 4, 4, 2), false),
                Arguments.of(a(0), true),
                Arguments.of(a(5), true),
                Arguments.of(a(5, 5), true),
                Arguments.of(a(10), true)
        );
    }

    @ParameterizedTest
    @MethodSource("rollValues")
    void addRoll(int[] rolls, boolean expectedValid) {
        GameHandler gameHandler = new GameHandler();
        Boolean actualValid = null;

        for (int roll: rolls) {
           actualValid = gameHandler.addRoll(roll);
        }

        assertNotNull(actualValid);
        assertEquals(expectedValid, actualValid);

    }

    private static int[] a(int... vals) {
        return vals;
    }
}
