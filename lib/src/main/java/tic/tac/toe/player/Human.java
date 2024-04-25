package tic.tac.toe.player;

import javax.annotation.Nullable;
import tic.tac.toe.data.Board;
import tic.tac.toe.data.Connection;
import tic.tac.toe.data.Mark;
import tic.tac.toe.data.Message;

public class Human implements Player {

  Connection conn;

  public Human(Connection conn) {
    this.conn = conn;
  }

  @Nullable
  public Integer getMoveOnce(Board board, Mark mark) {
    var userInput = conn.prompt(Message.MSG_PROMPT_MOVE, mark);

    int input;
    try {
      input = Integer.parseInt(userInput);
    } catch (NumberFormatException e) {
      conn.print(Message.ERR_NOT_A_NUMBER);
      return null;
    }

    input -= 1;

    if (input < 0 || input >= Board.SIZE) {
      conn.print(Message.ERR_NUMBER_OUT_OF_RANGE);
      return null;
    }

    if (!board.canMark(input)) {
      conn.print(Message.ERR_SPACE_OCCUPIED);
      return null;
    }

    return input;
  }

  public int getMove(Board board, Mark mark) {
    @Nullable
    Integer move;
    do {
      move = getMoveOnce(board, mark);
    } while (move == null);
    return move;
  }
}
