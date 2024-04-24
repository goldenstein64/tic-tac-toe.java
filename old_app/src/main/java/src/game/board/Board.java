/*
 * Name: James Ramsauer
 * Date: 9-17-2021
 * Course: CSC-331-001
 * Description: A tic-tac-toe game.
 */

package src.game.board;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import src.util.Lists;

/**
 * The class that implements the data and analysis portions of the
 * tic-tac-toe game.
 */
public class Board {

  /** the data used for the tic-tac-toe game */
  private List<Optional<Mark>> board = Lists.create(9, Optional.<Mark>empty());

  /**
   * An array that stores every possible horizontal, vertical, and diagonal win
   * combination.
   */
  public static final List<List<Integer>> winPatterns = List.of(
    List.of(0, 1, 2),
    List.of(3, 4, 5),
    List.of(6, 7, 8),
    List.of(0, 3, 6),
    List.of(1, 4, 7),
    List.of(2, 5, 8),
    List.of(0, 4, 8),
    List.of(2, 4, 6)
  );

  /** creates a new tic-tac-toe game */
  public Board() {
    // does nothing
  }

  public static Board fromPattern(String pattern) {
    Board result = new Board();

    return result;
  }

  /**
   * assigns a position on the tic-tac-toe board the given mark
   *
   * @param position The position to mark on the board.
   * @param mark     The mark to assign from.
   *
   * @throws IllegalArgumentException if the chosen space is already filled.
   */
  public void setMark(int position, Mark mark) throws IllegalArgumentException {
    assert canMark(position) : "This space is already filled!";

    board.set(position, Optional.of(mark));
  }

  /**
   * Describes whether a position on the tic-tac-toe board can be marked.
   *
   * @param position The position to check.
   *
   * @return Whether the position was marked.
   */
  public boolean canMark(int position) {
    return isMarkedWith(position, Optional.empty());
  }

  public boolean isMarkedWith(int position, Optional<Mark> mark) {
    return position >= 0 && position < 9 && board.get(position).equals(mark);
  }

  public boolean isMarkedWith(int position, Mark mark) {
    return isMarkedWith(position, Optional.of(mark));
  }

  public boolean won(Mark mark) {
    return winPatterns
      .stream()
      .anyMatch(pattern ->
        pattern
          .stream()
          .map(i -> board.get(i))
          .allMatch(found -> found.isPresent() && found.get() == mark)
      );
  }

  /**
   * Describes whether the board is full and no more moves can be made.
   *
   * @return Whether the board is full.
   */
  public boolean full() {
    return board.stream().allMatch(mark -> mark.isPresent());
  }

  /**
   * Describes whether the game ended for this mark.
   *
   * @param lastMark The last mark that was played.
   * @return Whether the game ended.
   */
  public boolean ended(Mark lastMark) {
    return won(lastMark) || full();
  }

  /**
   * Gives an ASCII representation of the tic-tac-toe board.
   *
   * @return The ASCII representation of the board.
   */
  public String toString() {
    return IntStream
      .range(0, 3)
      .boxed()
      .map(row ->
        IntStream
          .range(row * 3, row * 3 + 3)
          .boxed()
          .map(i ->
            board.get(i).map(m -> m.toString()).orElse(String.valueOf(i + 1))
          )
          .collect(Collectors.joining(" | ", " ", "\n"))
      )
      .collect(Collectors.joining("---|---|---\n"));
  }
}
