package tic.tac.toe.player;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import tic.tac.toe.data.Board;
import tic.tac.toe.data.Mark;
import tic.tac.toe.data.Message.*;
import tic.tac.toe.data.MessageException;
import tic.tac.toe.util.MockConnection;

public class HumanTest {

	public static class GetMoveOnce {

		@Test
		void asksConnForMove() throws MessageException {
			var conn = new MockConnection("2");
			var human = new Human(conn);
			var board = new Board();

			var move = human.getMoveOnce(board, Mark.X);

			assertEquals(List.of(new MSG_PromptMove(Mark.X)), conn.outputs);
			assertEquals(1, move);
		}

		@Test
		void printsPositionIsOccupied() {
			var conn = new MockConnection("3");
			var human = new Human(conn);
			var board = new Board(",,X,,,,,,");

			var exception = assertThrows(
					MessageException.class,
					() -> human.getMoveOnce(board, Mark.O));

			assertEquals(exception.message, new ERR_SpaceOccupied(3));
			assertEquals(List.of(new MSG_PromptMove(Mark.O)), conn.outputs);
		}

		@Test
		void printsPositionOutOfRange() {
			var conn = new MockConnection("0");
			var human = new Human(conn);
			var board = new Board();

			var exception = assertThrows(
					MessageException.class,
					() -> human.getMoveOnce(board, Mark.X));

			assertEquals(exception.message, new ERR_NumberOutOfRange(0));
			assertEquals(List.of(new MSG_PromptMove(Mark.X)), conn.outputs);
		}

		@Test
		void printsNaN_onHugeInt() throws MessageException {
			var input = "999999999999999999999999999999999999999999999999999";
			var conn = new MockConnection(input);
			var human = new Human(conn);
			var board = new Board();

			var exception = assertThrows(
					MessageException.class,
					() -> human.getMoveOnce(board, Mark.X));

			assertEquals(exception.message, new ERR_NotANumber(input));
			assertEquals(List.of(new MSG_PromptMove(Mark.X)), conn.outputs);
		}

		@Test
		void printsNaN_onBadInput() {
			var conn = new MockConnection("@");
			var human = new Human(conn);
			var board = new Board();

			var exception = assertThrows(
					MessageException.class,
					() -> human.getMoveOnce(board, Mark.X));

			assertEquals(exception.message, new ERR_NotANumber("@"));
			assertEquals(List.of(new MSG_PromptMove(Mark.X)), conn.outputs);
		}
	}
}
