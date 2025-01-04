package tic.tac.toe.player;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.collect.Streams;

import tic.tac.toe.data.Board;
import tic.tac.toe.data.Mark;

public class HardComputerTest {
    public static class TerminalTest {
        @Test
        public void fullBoardReturnsZero() {
            var board = new Board("OXXXOOOXX");
            var terminal = HardComputer.terminal(board);
            assertEquals(Optional.of(0), terminal);
        }

        @Test
        public void boardThatXWonReturnsOne() {
            var board = new Board("X,,XOOX,,");
            var terminal = HardComputer.terminal(board);
            assertEquals(Optional.of(1), terminal);
        }

        @Test
        public void fullBoardThatXWonReturnsOne() {
            var board = new Board("XXXXOOOXO");
            var terminal = HardComputer.terminal(board);
            assertEquals(Optional.of(1), terminal);
        }

        @Test
        public void boardThatOWonReturnsNegOne() {
            var board = new Board("O,,OXXO,X");
            var terminal = HardComputer.terminal(board);
            assertEquals(Optional.of(-1), terminal);
        }

        @Test
        public void fullBoardThatOWonReturnsNegOne() {
            var board = new Board("XXOXOXOOX");
            var terminal = HardComputer.terminal(board);
            assertEquals(Optional.of(-1), terminal);
        }

        @Test
        public void inProgressBoardReturnsEmpty() {
            var board = new Board("XX,OO,XX,");
            var terminal = HardComputer.terminal(board);
            assertEquals(Optional.empty(), terminal);
        }
    }

    public static class ResultOfTest {
        static final Random rng = new Random();

        static record Entry<T>(int index, T value) {
        }

        static final int TRIALS = 50;

        public static Stream<Object[]> testCases() {
            var currentTrials = 0;
            final var builder = Stream.<Object[]>builder();
            while (currentTrials < TRIALS) {
                final var options = List.<Optional<Mark>>of(
                        Optional.of(Mark.X),
                        Optional.of(Mark.O),
                        Optional.empty());

                final var initialValues = Stream.generate(
                        () -> options.get(rng.nextInt(options.size())))
                        .limit(Board.SIZE).toList();

                final var emptyIndexes = Streams.mapWithIndex(initialValues.stream(),
                        (v, i) -> new Entry<Optional<Mark>>((int) i, v))
                        .filter((e) -> e.value.isEmpty())
                        .toList();

                if (emptyIndexes.size() <= 0)
                    continue;

                assertTrue(1 <= emptyIndexes.size() && emptyIndexes.size() <= Board.SIZE);
                final var chosenIndex = emptyIndexes.get(rng.nextInt(emptyIndexes.size())).index;

                final var expectedValues = new ArrayList<Optional<Mark>>(initialValues);
                final var chosenMark = rng.nextInt(2) == 0 ? Mark.X : Mark.O;
                expectedValues.set(chosenIndex, Optional.of(chosenMark));

                final var initial = new Board(initialValues);
                final var expected = new Board(expectedValues);

                builder.add(new Object[] { initial, chosenMark, chosenIndex, expected });
                currentTrials++;
            }

            return builder.build();
        }

        @ParameterizedTest
        @MethodSource("testCases")
        public void isCalculatedCorrectly(Board initial, Mark chosenMark, int chosenIndex, Board expected) {
            final var actual = HardComputer.resultOf(initial, chosenMark, chosenIndex);
            assertEquals(expected, actual);
        }
    }
}
