package tic.tac.toe.data;

import java.util.Optional;

public sealed interface Message {
	public final record MSG_PromptPlayer(Mark mark) implements Message {
	}

	public final record MSG_PromptComputer(Mark mark) implements Message {
	}

	public final record MSG_PlayerWon(Optional<Mark> winner) implements Message {
	}

	public final class MSG_Tied implements Message {
		public static final MSG_Tied INSTANCE = new MSG_Tied();

		private MSG_Tied() {
		};
	}

	public final record MSG_Board(Board board) implements Message {
	}

	public final record ERR_PlayerInvalid(String input) implements Message {
	}

	public final record ERR_ComputerInvalid(String input) implements Message {
	}

	public final record MSG_PromptMove(Mark mark) implements Message {
	}

	public final record ERR_NotANumber(String input) implements Message {
	}

	public final record ERR_NumberOutOfRange(int choice) implements Message {
	}

	public final record ERR_SpaceOccupied(int choice) implements Message {
	}
}
