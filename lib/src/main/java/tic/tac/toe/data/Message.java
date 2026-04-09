package tic.tac.toe.data;

public sealed interface Message {
	public static final record MSG_PromptPlayer(Mark mark) implements Message {
	}

	public static final record MSG_PromptComputer(Mark mark) implements Message {
	}

	public static final record MSG_PlayerWon(Mark winner) implements Message {
	}

	public static enum StaticMessage implements Message {
		MSG_Tied
	}

	public static StaticMessage MSG_Tied = StaticMessage.MSG_Tied;

	public static final record MSG_Board(String pattern) implements Message {
	}

	public static final record ERR_PlayerInvalid(String input) implements Message {
	}

	public static final record ERR_ComputerInvalid(String input) implements Message {
	}

	public static final record MSG_PromptMove(Mark mark) implements Message {
	}

	public static final record ERR_NotANumber(String input) implements Message {
	}

	public static final record ERR_NumberOutOfRange(int choice) implements Message {
	}

	public static final record ERR_SpaceOccupied(int choice) implements Message {
	}
}
