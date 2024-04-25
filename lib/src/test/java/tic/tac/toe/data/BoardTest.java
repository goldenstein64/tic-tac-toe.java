package tic.tac.toe.data;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class BoardTest {

	public static class Empty {

		@Test
		void detectsEmpty() {
			Board board = new Board();
			assertTrue(board.empty());
		}

		@ParameterizedTest
		@ValueSource(
			strings = {
				"XO,XO,XO,", "XXXXXXXX,", ",XXXXXXXX", "X,,,,,,,,", ",,,,,,,,X",
			}
		)
		void detectsNonEmpty(String pattern) {
			var board = new Board(pattern);
			assertFalse(board.empty());
		}
	}

	public static class Full {

		@ParameterizedTest
		@ValueSource(strings = { "XXOOOXXXO", "XXOOOXXOX", "XXOOXXXOO" })
		void detectsFull(String pattern) {
			var board = new Board(pattern);
			assertTrue(board.full());
		}

		@ParameterizedTest
		@ValueSource(
			strings = {
				"XO,XO,XO,", "XXXXXXXX,", ",XXXXXXXX", "X,,,,,,,,", ",,,,,,,,X",
			}
		)
		void detectsNotFull(String pattern) {
			var board = new Board(pattern);
			assertFalse(board.full());
		}
	}

	public static class IsMarkedWith {

		static Stream<Arguments> provideBadPos() {
			return Stream.of(
				Arguments.of(-1, false),
				Arguments.of(0, true),
				Arguments.of(1, true),
				Arguments.of(7, true),
				Arguments.of(8, true),
				Arguments.of(9, false)
			);
		}

		@ParameterizedTest
		@MethodSource("provideBadPos")
		void falseOnBadPosition(int pos, boolean expected) {
			assertEquals(expected, new Board().isMarkedWith(pos, null));
		}

		static Stream<Arguments> provideMarkMatch() {
			return Stream.of(
				Arguments.of(0, Mark.X, true),
				Arguments.of(0, Mark.O, false),
				Arguments.of(0, null, false),
				Arguments.of(1, Mark.X, false),
				Arguments.of(1, Mark.O, true),
				Arguments.of(1, null, false),
				Arguments.of(2, Mark.X, false),
				Arguments.of(2, Mark.O, false),
				Arguments.of(2, null, true)
			);
		}

		@ParameterizedTest
		@MethodSource("provideMarkMatch")
		void matchesOnMark(int pos, @Nullable Mark mark, boolean expected) {
			var board = new Board("XO,XO,XO,");
			assertEquals(expected, board.isMarkedWith(pos, mark));
		}
	}

	public static class CanMark {

		static Stream<Arguments> provideMatchNull() {
			return Stream.of(
				Arguments.of(0, false),
				Arguments.of(1, false),
				Arguments.of(2, true),
				Arguments.of(3, false),
				Arguments.of(4, false),
				Arguments.of(5, true),
				Arguments.of(6, false),
				Arguments.of(7, false),
				Arguments.of(8, true)
			);
		}

		@ParameterizedTest
		@MethodSource("provideMatchNull")
		void matchesOnNull(int pos, boolean expected) {
			var board = new Board("XO,XO,XO,");
			assertEquals(expected, board.canMark(pos));
		}
	}

	public static class Set {

		@Test
		void throwsOnOccupied() {
			var board = new Board(",,X,,,,,,");

			assertThrows(IllegalArgumentException.class, () -> board.set(2, Mark.O));

			board.set(1, Mark.O);
		}

		@Test
		void throwsOnBadPosition() {
			var board = new Board();

			assertThrows(
				IndexOutOfBoundsException.class,
				() -> board.set(-1, Mark.X)
			);

			board.set(0, Mark.X);
		}

		static Stream<Arguments> provideChangeState() {
			return Stream.iterate(0, i -> i + 1).limit(9).map(Arguments::of);
		}

		@ParameterizedTest
		@MethodSource("provideChangeState")
		void changesState(int i) {
			var board = new Board();
			assertTrue(board.canMark(i));
			board.set(i, Mark.X);
			assertFalse(board.canMark(i));
		}
	}
}
