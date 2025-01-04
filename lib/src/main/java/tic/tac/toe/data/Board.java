package tic.tac.toe.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
		this.data = new ArrayList<Mark>(Stream.<Mark>generate(() -> null).limit(SIZE).toList());
	}

	public Board(List<Optional<Mark>> data) {
		this.data = new ArrayList<Mark>(data.stream()
				.map(opt -> opt.orElse(null))
				.toList());
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
		this.data = new ArrayList<Mark>(stream.toList());
	}

	public Board(Board board) {
		this.data = new ArrayList<Mark>(board.data);
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
			throw new IndexOutOfBoundsException("slot is out of bounds!");
		} else if (data.get(pos) != null) {
			throw new IllegalArgumentException("slot is occupied!");
		}
		data.set(pos, mark);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (!(object instanceof Board)) {
			return false;
		} else {
			Board other = (Board) object;
			return this.data.equals(other.data);
		}
	}

	@Nullable
	public Mark get(int pos) {
		return data.get(pos);
	}

	@Override
	public String toString() {
		return data
				.stream()
				.<Character>map(m -> m == null ? ',' : switch (m) {
					case X -> 'X';
					case O -> 'O';
				})
				.collect(STRING_COLLECTOR);
	}
}
