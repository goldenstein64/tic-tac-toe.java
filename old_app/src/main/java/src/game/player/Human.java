/*
 * Name: James Ramsauer
 * Date: 9-17-2021
 * Course: CSC-331-001
 * Description: A human-controlled opponent.
 */

package src.game.player;

import src.game.board.Board;
import src.game.board.Mark;
import src.util.IO;

/**
 * Class that implements a human-controlled opponent.
 */
public class Human implements Player {

  public static IO io = new IO()
    .register("msg.pickMove", "Pick a move, Player %s [1-9]: ")
    .register("err.invalidMove", "This does not match [1-9]!")
    .register("err.occupied", "This space cannot be filled!");

  private Board board;
  private final Mark mark;

  /**
   * Creates a new Human Player.
   *
   * @param mark The mark this player uses.
   */
  public Human(Board board, Mark mark) {
    this.board = board;
    this.mark = mark;
  }

  /**
   * Asks for a valid move from this Human until it gets one.
   *
   * @return The position found.
   */
  public int getMove() {
    while (true) {
      // prompt question

      int pos;
      try {
        String posString = io.prompt("msg.pickMove", mark);
        pos = Integer.parseInt(posString);
      } catch (NumberFormatException e) {
        io.print("err.invalidMove");
        continue;
      }

      int result = pos - 1;

      if (!board.canMark(result)) {
        io.print("err.occupied");
        continue;
      }

      return result;
    }
  }
}
