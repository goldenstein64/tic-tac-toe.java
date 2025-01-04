package tic.tac.toe.player;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.google.common.collect.Streams;

import tic.tac.toe.data.Board;
import tic.tac.toe.data.Mark;

public class HardComputer implements Player {
    static record Equality(int a, int b) {
    }

    static record Symmetry(List<Integer> equalities, List<Integer> image) {
    }

    static record Entry<T>(int index, T value) {
    }

    static record ActionScorePair(int action, int score) {
    }

    static <T> Stream<Entry<T>> streamEntries(List<T> values) {
        return IntStream.range(0, values.size()).boxed()
                .map(i -> new Entry<T>(i, values.get(i)));
    }

    static final List<Equality> EQUALITIES = List.of(
            new Equality(0, 2),
            new Equality(3, 5),
            new Equality(6, 8),
            new Equality(0, 6),
            new Equality(1, 7),
            new Equality(2, 8),
            new Equality(1, 3),
            new Equality(2, 6),
            new Equality(5, 7),
            new Equality(3, 7),
            new Equality(0, 8),
            new Equality(1, 5));

    static final List<Symmetry> SYMMETRIES = List.of(
            new Symmetry( // rotate 90
                    List.of(0, 1, 2, 6, 7, 8),
                    List.of(0, 1, 4)),
            new Symmetry( // rotate 180
                    List.of(1, 4, 7, 10),
                    List.of(0, 1, 2, 3, 4)),
            new Symmetry( // vertical
                    List.of(0, 1, 2),
                    List.of(0, 1, 3, 4, 6, 7)),
            new Symmetry( // horizontal
                    List.of(3, 4, 5),
                    List.of(0, 1, 2, 3, 4, 5)),
            new Symmetry( // diagonal down
                    List.of(6, 7, 8),
                    List.of(0, 1, 2, 3, 4, 6)),
            new Symmetry( // diagonal up
                    List.of(9, 10, 11),
                    List.of(0, 1, 2, 4, 5, 8)));

    static int controls(Mark mark) {
        return switch (mark) {
            case X -> -1;
            case O -> 1;
        };
    }

    static BinaryOperator<Integer> reconcilers(Mark mark) {
        return switch (mark) {
            case X -> Math::max;
            case O -> Math::min;
        };
    }

    static boolean symmetryMatches(HashSet<Integer> equalSet, Stream<Integer> symmetry) {
        return symmetry.allMatch(equalSet::contains);
    }

    static Stream<Integer> filterImage(Board board, Stream<Integer> image) {
        return image.filter(board::canMark);
    }

    static HashSet<Integer> equalitySetOf(Board board) {
        return new HashSet<Integer>(streamEntries(EQUALITIES)
                .filter(entry -> {
                    var equality = entry.value;
                    return board.get(equality.a) == board.get(equality.b);
                })
                .map(entry -> entry.index)
                .toList());
    }

    static Optional<Stream<Integer>> symmetricActions(Board board) {
        final var equalitySet = equalitySetOf(board);
        final var maybeMatchedSymmetry = SYMMETRIES.stream().filter(
                sym -> symmetryMatches(equalitySet, sym.equalities.stream()))
                .findFirst();

        return maybeMatchedSymmetry.map(
                sym -> filterImage(board, sym.image.stream()));
    }

    static Stream<Integer> simpleActions(Board board) {
        return IntStream.range(0, Board.SIZE).boxed()
                .filter(board::canMark);
    }

    static Stream<Integer> actions(Board board) {
        return symmetricActions(board).orElse(simpleActions(board));
    }

    public static Board resultOf(Board board, Mark mark, int action) {
        final var newBoard = new Board(board);
        newBoard.set(action, mark);
        return newBoard;
    }

    public static Optional<Integer> terminal(Board board) {
        if (board.won(Mark.X)) {
            return Optional.of(1);
        } else if (board.won(Mark.O)) {
            return Optional.of(-1);
        } else if (board.full()) {
            return Optional.of(0);
        } else {
            return Optional.empty();
        }
    }

    static int judge(Board board, Mark mark) {
        final var terminal = terminal(board);
        final var otherMark = mark.other();

        return terminal.orElseGet(() -> actions(board)
                .map(action -> resultOf(board, mark, action))
                .map(newBoard -> judge(newBoard, otherMark))
                .reduce(controls(mark), reconcilers(mark)));
    }

    public static List<Integer> getMoves(Board board, Mark mark) {
        final var otherMark = mark.other();

        final var actions = simpleActions(board).toList();
        final var scores = actions.stream()
                .map(action -> resultOf(board, mark, action))
                .map(newBoard -> judge(newBoard, otherMark))
                .toList();

        final var bestScore = scores.stream()
                .reduce(controls(mark), reconcilers(mark));
        return Streams.zip(
                actions.stream(), scores.stream(),
                (action, score) -> new ActionScorePair(action, score))
                .filter(p -> p.score == bestScore)
                .map(p -> p.action)
                .toList();
    }

    Random rng;

    public HardComputer(Random rng) {
        this.rng = rng;
    }

    public int getMove(Board board, Mark mark) {
        if (board.empty()) {
            return rng.nextInt(Board.SIZE);
        } else {
            final var moves = getMoves(board, mark);
            if (moves.size() <= 0) {
                throw new NoSuchElementException("no moves to take!");
            }
            return moves.get(rng.nextInt(moves.size()));
        }
    }
}
