package service;

import model.RoundScore;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrintServiceTest {

    private static Stream<Arguments> scoreBlockInput() {
        return Stream.of(
                Arguments.of(167, 5, "  167  |"),
                Arguments.of(67, 5, "   67  |"),
                Arguments.of(7, 5, "    7  |"),
                Arguments.of(167, 10, "    167     |"),
                Arguments.of(67, 10, "     67     |"),
                Arguments.of(7, 10, "      7     |")
        );
    }

    @ParameterizedTest
    @MethodSource("scoreBlockInput")
    void scoreBlock(int score, int frame, String expected) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = PrintService.class.getDeclaredMethod("scoreBlock", int.class, int.class);
        method.setAccessible(true);
        String actual = (String) method.invoke(PrintService.class, score, frame);
        assertEquals(expected, actual);
    }

    private static RoundScore rs(int... rolls) {
        RoundScore score = new RoundScore(rolls.length);
        for (int roll : rolls) score.addRoll(roll);
        return score;
    }

    private static Stream<Arguments> rollBlockInput() {
        return Stream.of(
                Arguments.of(rs(5, 4), 8, " [5][4]|"),
                Arguments.of(rs(6, 4), 8, " [6][/]|"),
                Arguments.of(rs(10), 8, " [ ][X]|"),
                Arguments.of(rs(10), 10, "  [X][ ][ ] |"),
                Arguments.of(rs(10, 10), 10, "  [X][X][ ] |"),
                Arguments.of(rs(10, 10, 10), 10, "  [X][X][X] |"),
                Arguments.of(rs(10, 5, 4), 10, "  [X][5][4] |"),
                Arguments.of(rs(10, 5, 5), 10, "  [X][5][/] |"),
                Arguments.of(rs(5, 5, 5), 10, "  [5][/][5] |"),
                Arguments.of(rs(5, 5, 10), 10, "  [5][/][X] |")
        );
    }

    @ParameterizedTest
    @MethodSource("rollBlockInput")
    void rollBlock(RoundScore score, int frame, String expected) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = PrintService.class.getDeclaredMethod("rollBlock", RoundScore.class, int.class);
        method.setAccessible(true);
        String actual = (String) method.invoke(PrintService.class, score, frame);
        assertEquals(expected, actual);
    }
}
