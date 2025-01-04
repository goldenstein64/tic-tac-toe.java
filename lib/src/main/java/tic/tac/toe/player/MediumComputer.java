package tic.tac.toe.player;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import tic.tac.toe.data.Board;
import tic.tac.toe.data.Mark;

public class MediumComputer extends Computer {
	public static final List<Integer> CENTER = List.of(4);
	public static final List<Integer> CORNERS = List.of(0, 2, 6, 8);
	public static final List<Integer> SIDES = List.of(1, 3, 5, 7);

	public static final List<List<List<Integer>>> WIN_PATTERNS_FOR = IntStream
			.range(0, Board.SIZE).boxed()
			.map(i -> Board.WIN_PATTERNS
					.stream()
					.filter(p -> p.contains(i))
					.map(p -> p.stream().filter(j -> j != i).toList())
					.toList())
			.toList();

	public MediumComputer(Random rng) {
		super(rng);
	}

	private <T> Optional<List<T>> listOptionOf(List<T> list) {
		return list.size() > 0 ? Optional.of(list) : Optional.empty();
	}

	private Optional<List<Integer>> filterOptionOf(
			List<Integer> list,
			Board board) {
		return listOptionOf(list.stream().filter(board::canMark).toList());
	}

	private Optional<List<Integer>> getWinningMoves(Board board, Mark mark) {
		var result = Stream
				.iterate(0, i -> i + 1)
				.limit(Board.SIZE)
				.filter(board::canMark)
				.filter(i -> {
					return WIN_PATTERNS_FOR
							.get(i)
							.stream()
							.anyMatch(pattern -> pattern.stream().map(j -> board.get(j)).allMatch(m -> m == mark));
				})
				.toList();

		return listOptionOf(result);
	}

	private Optional<List<Integer>> getBlockingMoves(Board board, Mark mark) {
		return getWinningMoves(board, mark.other());
	}

	private Optional<List<Integer>> getTrappingMoves(Board board, Mark mark) {
		var result = Stream
				.iterate(0, i -> i + 1)
				.limit(Board.SIZE)
				.filter(board::canMark)
				.filter(i -> {
					var trapCount = WIN_PATTERNS_FOR
							.get(i)
							.stream()
							.filter(pattern -> pattern.stream().anyMatch(board::canMark) &&
									pattern.stream().anyMatch(j -> board.isMarkedWith(j, mark)))
							.count();

					return trapCount > 1;
				})
				.toList();

		return listOptionOf(result);
	}

	private Optional<List<Integer>> getCenterMove(Board board, Mark mark) {
		return filterOptionOf(CENTER, board);
	}

	private Optional<List<Integer>> getCornerMoves(Board board, Mark mark) {
		return filterOptionOf(CORNERS, board);
	}

	private Optional<List<Integer>> getSideMoves(Board board, Mark mark) {
		return filterOptionOf(SIDES, board);
	}

	@Override
	public List<Integer> getMoves(Board board, Mark mark) {
		return getWinningMoves(board, mark)
				.or(() -> getBlockingMoves(board, mark))
				.or(() -> getTrappingMoves(board, mark))
				.or(() -> getCenterMove(board, mark))
				.or(() -> getCornerMoves(board, mark))
				.or(() -> getSideMoves(board, mark))
				.orElseThrow();
	}
}
