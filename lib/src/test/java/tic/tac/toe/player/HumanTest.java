package tic.tac.toe.player;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import tic.tac.toe.data.Board;
import tic.tac.toe.data.Mark;
import tic.tac.toe.data.Message;
import tic.tac.toe.util.MockConnection;

public class HumanTest {

	public static class GetMoveOnce {

		@Test
		void asksConnForMove() {
			var conn = new MockConnection("2");
			var human = new Human(conn);
			var board = new Board();

			var move = human.getMoveOnce(board, Mark.X);

			assertEquals(List.of(Message.MSG_PROMPT_MOVE), conn.outputs);
			assertEquals(1, move);
		}

		@Test
		void printsPositionIsOccupied() {
			var conn = new MockConnection("3");
			var human = new Human(conn);
			var board = new Board(",,X,,,,,,");

			var move = human.getMoveOnce(board, Mark.O);

			assertEquals(
				List.of(Message.MSG_PROMPT_MOVE, Message.ERR_SPACE_OCCUPIED),
				conn.outputs
			);
			assertNull(move);
		}

		@Test
		void printsPositionOutOfRange() {
			var conn = new MockConnection("0");
			var human = new Human(conn);
			var board = new Board();

			var move = human.getMoveOnce(board, Mark.X);

			assertEquals(
				List.of(Message.MSG_PROMPT_MOVE, Message.ERR_NUMBER_OUT_OF_RANGE),
				conn.outputs
			);
			assertNull(move);
		}

		@Test
		void printsNaN_onHugeInt() {
			var conn = new MockConnection(
				"999999999999999999999999999999999999999999999999999"
			);
			var human = new Human(conn);
			var board = new Board();

			var move = human.getMoveOnce(board, Mark.X);

			assertEquals(
				List.of(Message.MSG_PROMPT_MOVE, Message.ERR_NOT_A_NUMBER),
				conn.outputs
			);
			assertNull(move);
		}

		@Test
		void printsNaN_onBadInput() {
			var conn = new MockConnection("@");
			var human = new Human(conn);
			var board = new Board();

			var move = human.getMoveOnce(board, Mark.X);

			assertEquals(
				List.of(Message.MSG_PROMPT_MOVE, Message.ERR_NOT_A_NUMBER),
				conn.outputs
			);
			assertNull(move);
		}
	}
}
