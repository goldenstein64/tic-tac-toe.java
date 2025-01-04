package tic.tac.toe.player;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import tic.tac.toe.data.Board;
import tic.tac.toe.data.Mark;

public class CommonTactics {

	public static Stream<Object[]> testCases() {
		return Stream.<Object[]>of(
				new Object[] { ",XX,OO,,,", Mark.X, 0 }, // X winning move
				new Object[] { ",OO,XX,,X", Mark.O, 0 }, // O winning move
				new Object[] { "O,,O,X,X,", Mark.X, 6 }, // X blocking move
				new Object[] { ",O,X,,XXO", Mark.O, 0 }, // O blocking move
				new Object[] { ",X,O,X,O,", Mark.X, 2 }, // X trapping move
				// - X -
				// O X X
				// - O -
				new Object[] { ",X,OXX,O,", Mark.O, 6 }, // O trapping move
				new Object[] { ",XXXOOOX,", Mark.O, 0 });
	}

	@ParameterizedTest
	@MethodSource("testCases")
	void MCpassesAllTests(String pattern, Mark mark, int expected) {
		var board = new Board(pattern);
		var computer = new MediumComputer(new Random());

		var move = computer.getMove(board, mark);

		assertEquals(expected, move);
	}
}
