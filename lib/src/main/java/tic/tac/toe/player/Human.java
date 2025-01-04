package tic.tac.toe.player;

import tic.tac.toe.data.Board;
import tic.tac.toe.data.Connection;
import tic.tac.toe.data.Mark;
import tic.tac.toe.data.Message;
import tic.tac.toe.data.MessageException;

public class Human implements Player {

	Connection conn;

	public Human(Connection conn) {
		this.conn = conn;
	}

	public int getMoveOnce(Board board, Mark mark) throws MessageException {
		var userInput = conn.prompt(new Message.MSG_PromptMove(mark));

		int input;
		try {
			input = Integer.parseInt(userInput);
		} catch (NumberFormatException e) {
			throw new MessageException(new Message.ERR_NotANumber(userInput));
		}

		if (input < 1 || input > Board.SIZE) {
			throw new MessageException(new Message.ERR_NumberOutOfRange(input));
		}

		if (!board.canMark(input - 1)) {
			throw new MessageException(new Message.ERR_SpaceOccupied(input));
		}

		return input - 1;
	}

	@Override
	public int getMove(Board board, Mark mark) {
		while (true) {
			try {
				return getMoveOnce(board, mark);
			} catch (MessageException exception) {
				conn.print(exception.message);
			}
		}
	}
}
