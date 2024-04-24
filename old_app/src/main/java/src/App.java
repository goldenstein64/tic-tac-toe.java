/*
 * Name: James Ramsauer
 * Date: 9-17-2021
 * Course: CSC-331-001
 * Description: The program that runs a tic-tac-toe game.
 */

package src;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import src.game.board.Board;
import src.game.board.Mark;
import src.game.player.Computer;
import src.game.player.Human;
import src.game.player.Player;
import src.util.IO;

/**
 * The program that implements the gameplay loop of the tic-tac-toe game.
 */
public class App {

  public static IO io = new IO()
    .register("msg.start", "This program runs a tic-tac-toe game.")
    .register("msg.waitKey", "Press Enter to exit: ")
    .register("msg.playerWon", "Player %s won!")
    .register("msg.tie", "There was a tie!")
    .register("msg.board", "%s")
    .register(
      "msg.pickPlayer",
      "Will Player %s be a human or computer? [H/C]: "
    )
    .register("err.invalidPlayer", "Expected 'H' or 'C', got '%s'");

  public static final Mark[] allPlayers = new Mark[] { Mark.X, Mark.O };

  /**
   * The entry point for the tic-tac-toe gameplay.
   *
   * @param args The arguments passed into the program invocation.
   */
  public static void main(String[] args) {
    io.print("msg.start");

    Optional<Mark> winner = playGame();

    displayWinner(winner);

    // let the player exit the program instead of automatically exiting once the
    // game ends.
    io.prompt("msg.waitKey");
  }

  /**
   * Chooses the type of players that will participate in this game.
   *
   * @param board The board that the player plays on
   * @return A dictionary of marks to players.
   */
  public static Map<Mark, Player> choosePlayers(Board board) {
    Map<Mark, Player> players = new HashMap<>();

    // for each playable mark,
    for (Mark mark : allPlayers) {
      Player chosenPlayer = null;
      while (true) {
        String chosen = io.prompt("msg.pickPlayer", mark);
        chosen = chosen.toUpperCase();

        if (chosen.equals("H")) {
          chosenPlayer = new Human(board, mark);
          break;
        } else if (chosen.equals("C")) {
          chosenPlayer = new Computer(board, mark);
          break;
        } else {
          io.print("err.invalidPlayer", chosen);
          continue;
        }
      }
      players.put(mark, chosenPlayer);
    }

    return players;
  }

  /**
   * Plays a game of tic-tac-toe with the chosen players.
   *
   * @param players The dictionary of players in the game.
   * @return The winner of the game
   */
  public static Optional<Mark> playGame() {
    Board board = new Board();

    Map<Mark, Player> players = choosePlayers(board);

    // display the game's starting state.
    io.print("msg.board", board);

    Mark currentMark = Mark.O;
    do {
      // flip the mark
      currentMark = currentMark.other();

      Player currentPlayer = players.get(currentMark);

      int position = currentPlayer.getMove();

      board.setMark(position, currentMark);

      // display the game's new state
      io.print("msg.board", board);
    } while (!board.ended(currentMark));

    if (board.won(currentMark)) {
      return Optional.of(currentMark);
    } else {
      return Optional.empty();
    }
  }

  /**
   * Prints out the winner of this game.
   *
   * @param winner Who won the game.
   */
  public static void displayWinner(Optional<Mark> winner) {
    if (winner.isPresent()) {
      io.print("msg.playerWon", winner.get());
    } else {
      io.print("msg.tie");
    }
  }
}
