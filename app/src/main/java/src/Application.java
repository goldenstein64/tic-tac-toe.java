package src;

import tic.tac.toe.data.Mark;
import tic.tac.toe.data.Message;
import tic.tac.toe.player.Player;

import static tic.tac.toe.data.Message.*;

import java.util.List;
import java.util.Optional;

public class Application {
  static final String ROW_FORMAT = " %s | %s | %s\n";
  static final String ROW_FORMAT_LAST = " %s | %s | %s";
  static final String ROW_SEPARATOR = "---|---|---\n";

  static String boardFormat = new StringBuilder()
      .append(" %c | %c | %c\n")
      .append("---|---|---\n")
      .append(" %c | %c | %c\n")
      .append("---|---|---\n")
      .append(" %c | %c | %c")
      .toString();

  static char parseAt(String pattern, int index) {
    return switch (pattern.charAt(index)) {
      case 'X' -> 'X';
      case 'O' -> 'O';
      default -> Integer.toString(index + 1).charAt(0);
    };
  }

  static String formatRow(String format, String pattern, int start) {
    return String.format(format, parseAt(pattern, start), parseAt(pattern, start + 1), parseAt(pattern, start + 2));
  }

  static String formatBoard(String pattern) {
    return new StringBuilder()
        .append(formatRow(ROW_FORMAT, pattern, 0))
        .append(ROW_SEPARATOR)
        .append(formatRow(ROW_FORMAT, pattern, 3))
        .append(ROW_SEPARATOR)
        .append(formatRow(ROW_FORMAT_LAST, pattern, 6))
        .toString();
  }

  public static void main(String[] args) {
    ConsoleConnection conn = new ConsoleConnection() {
      public String format(Message message) {
        if (message instanceof MSG_PromptPlayer msg) {
          return String.format("Is Player %s a human or computer? [H/C]: ", msg.mark());
        } else if (message instanceof MSG_PromptComputer msg) {
          return String.format("What is Computer %s's difficulty? [E/M/H]: ", msg.mark());
        } else if (message instanceof MSG_PlayerWon msg) {
          return String.format("Player %s won!", msg.winner());
        } else if (message == MSG_Tied) {
          return "There was a tie!";
        } else if (message instanceof MSG_Board msg) {
          return formatBoard(msg.pattern());
        } else if (message instanceof ERR_PlayerInvalid) {
          return "This does not match 'H' or 'C'!";
        } else if (message instanceof ERR_ComputerInvalid) {
          return "This does not match 'E', 'M' or 'H'!";
        } else if (message instanceof MSG_PromptMove msg) {
          return String.format("Pick a move, Player %s [1-9]: ", msg.mark());
        } else if (message instanceof ERR_NotANumber msg) {
          return String.format("'%s' is not a valid number!", msg.input());
        } else if (message instanceof ERR_NumberOutOfRange msg) {
          return String.format("%d is not in the range of 1-9!", msg.choice());
        } else if (message instanceof ERR_SpaceOccupied msg) {
          return String.format("Slot %d is occupied!", msg.choice());
        } else {
          throw new RuntimeException("Message not recognized!");
        }
      }
    };
    tic.tac.toe.Application app = new tic.tac.toe.Application(conn);
    List<Player> players = app.choosePlayers();
    Optional<Mark> winner = app.playGame(players);
    app.displayWinner(winner);
  }
}
