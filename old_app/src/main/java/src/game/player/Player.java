/*
 * Name: James Ramsauer
 * Date: 9-17-2021
 * Course: CSC-331-001
 * Description: The contract the game uses to let players make a decision.
 */

package src.game.player;

import src.game.board.Board;

/**
 * An interface that describes how the game loop interacts with players in a
 * tic-tac-toe game.
 */
public interface Player {
  /**
   * Gives this object's next intended move.
   *
   * @return The position they want to put a mark in.
   */
  public int getMove();
}
