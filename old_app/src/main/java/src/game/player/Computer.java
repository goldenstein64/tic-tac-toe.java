/*
 * Name: James Ramsauer
 * Date: 9-17-2021
 * Course: CSC-331-001
 * Description: A computer-controlled opponent.
 */

package src.game.player;

import java.security.SecureRandom;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import src.game.board.Board;
import src.game.board.Mark;
import src.util.BoardCountCollector;
import src.util.Lists;

/**
 * Class that implements a computer-controlled opponent
 */
public class Computer implements Player {

  private final SecureRandom rng = new SecureRandom();

  // properties for positions on the board organized by value
  private static final List<Integer> center = List.of(4);
  private static final List<Integer> corners = List.of(0, 2, 6, 8);
  private static final List<Integer> sides = List.of(1, 3, 5, 7);

  private final Board board;
  private final Mark mark;
  private final Mark opponentMark;

  private List<Supplier<List<Integer>>> moveGetters = List.of(
    this::getWinningMoves,
    this::getBlockingMoves,
    this::getTrappingMoves,
    this::getCenterMoves,
    this::getCornerMoves,
    this::getSideMoves
  );

  /**
   * A dictionary of all win patterns given the key has this mark.
   */
  public static final List<List<List<Integer>>> winPatternsWith = List.of(
    List.of(List.of(1, 2), List.of(3, 6), List.of(4, 8)),
    List.of(List.of(0, 2), List.of(4, 7)),
    List.of(List.of(0, 1), List.of(4, 6), List.of(5, 8)),
    List.of(List.of(0, 6), List.of(4, 5)),
    List.of(List.of(0, 8), List.of(1, 7), List.of(2, 6), List.of(3, 5)),
    List.of(List.of(2, 8), List.of(3, 4)),
    List.of(List.of(0, 3), List.of(2, 4), List.of(7, 8)),
    List.of(List.of(1, 4), List.of(6, 8)),
    List.of(List.of(0, 4), List.of(2, 5), List.of(6, 7))
  );

  public static final Supplier<Stream<Integer>> boardRange = () ->
    IntStream.range(0, 9).boxed();

  /**
   * Creates a new opponent for the given game.
   *
   * @param board The game this opponent will participate in.
   */
  public Computer(Board board, Mark mark) {
    this.board = board;
    this.mark = mark;
    this.opponentMark = mark.other();
  }

  private List<Integer> getOpeningCounts() {
    return boardRange
      .get()
      .filter(i -> board.isMarkedWith(i, this.mark))
      .map(winPatternsWith::get)
      .flatMap(patterns ->
        patterns
          .stream()
          .filter(pattern -> pattern.stream().allMatch(board::canMark))
          .flatMap(pattern -> pattern.stream())
      )
      .collect(new BoardCountCollector());
  }

  private List<Integer> getOpeningsWithCount(int count) {
    return getOpeningCounts().stream().filter(found -> found >= count).toList();
  }

  /**
   * Finds every position the computer can use to win the game in one turn.
   *
   * @param forMark The mark to search winning patterns with.
   *
   * @return The array of winning moves.
   */
  private List<Integer> getWinningMovesFor(Mark forMark) {
    return boardRange
      .get()
      .filter(pos -> board.canMark(pos))
      .filter(i ->
        Computer.winPatternsWith
          .get(i)
          .stream()
          .anyMatch(pattern ->
            pattern.stream().allMatch(pos -> board.isMarkedWith(pos, forMark))
          )
      )
      .toList();
  }

  /**
   * Processes and gives what the Computer thinks is the best move.
   *
   * @return The Computer's best move.
   */
  public int getMove() {
    for (Supplier<List<Integer>> moveGetter : moveGetters) {
      final List<Integer> moves = moveGetter.get();
      if (Lists.found(moves)) return Lists.choose(moves, rng);
    }

    throw new RuntimeException("No possible moves to pick from!");
  }

  private List<Integer> getWinningMoves() {
    return getWinningMovesFor(this.mark);
  }

  private List<Integer> getBlockingMoves() {
    return getWinningMovesFor(this.opponentMark);
  }

  /**
   * Returns an array of all moves that would create a winning move on the next
   * turn.
   *
   * @return The array of trapping moves.
   */
  private List<Integer> getTrappingMoves() {
    return getOpeningsWithCount(2);
  }

  private List<Integer> getCenterMoves() {
    return center.stream().filter(board::canMark).toList();
  }

  private List<Integer> getCornerMoves() {
    return corners.stream().filter(board::canMark).toList();
  }

  private List<Integer> getSideMoves() {
    return sides.stream().filter(board::canMark).toList();
  }
}
