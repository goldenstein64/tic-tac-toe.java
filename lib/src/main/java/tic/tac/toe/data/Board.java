package tic.tac.toe.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Board {

	private static final Collector<Character, StringBuilder, String> STRING_COLLECTOR = Collector.of(
			StringBuilder::new,
			StringBuilder::append,
			StringBuilder::append,
			StringBuilder::toString);

	public static final int SIZE = 9;
	public static final List<List<Integer>> WIN_PATTERNS = List.of(
			List.of(0, 1, 2),
			List.of(3, 4, 5),
			List.of(6, 7, 8),
			List.of(0, 3, 6),
			List.of(1, 4, 7),
			List.of(2, 5, 8),
			List.of(2, 4, 6),
			List.of(0, 4, 8));

	private List<Mark> data;

	public Board() {
		var stream = Stream.<Mark>generate(() -> null).limit(SIZE);
		data = new ArrayList<Mark>(stream.toList());
	}

	public Board(String pattern) {
		var stream = pattern
				.chars()
				.limit(SIZE)
				.<Mark>mapToObj(c -> switch (c) {
					case 'X' -> Mark.X;
					case 'O' -> Mark.O;
					default -> null;
				});
		data = new ArrayList<Mark>(stream.toList());
	}

	public Board(Board board) {
		data = new ArrayList<Mark>(board.data);
	}

	public boolean empty() {
		return data.stream().allMatch(m -> m == null);
	}

	public boolean full() {
		return data.stream().allMatch(m -> m != null);
	}

	public boolean won(@Nonnull Mark mark) {
		return WIN_PATTERNS
				.stream()
				.anyMatch(pattern -> pattern
						.stream()
						.map(data::get)
						.allMatch((@Nullable Mark m) -> m == mark));
	}

	public boolean isMarkedWith(int pos, @Nullable Mark mark) {
		return pos >= 0 && pos < SIZE && Objects.equals(mark, data.get(pos));
	}

	public boolean canMark(int pos) {
		return isMarkedWith(pos, null);
	}

	public void set(int pos, @Nullable Mark mark) {
		if (pos < 0 || pos >= SIZE) {
			throw new IndexOutOfBoundsException();
		} else if (data.get(pos) != null) {
			throw new IllegalArgumentException();
		}
		data.set(pos, mark);
	}

	@Nullable
	public Mark get(int pos) {
		return data.get(pos);
	}

	public String toString() {
		return data
				.stream()
				.<Character>map(m -> switch (m) {
					case X -> 'X';
					case O -> 'O';
					default -> ',';
				})
				.collect(STRING_COLLECTOR);
	}
}
