package tic.tac.toe.player;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import tic.tac.toe.data.Board;
import tic.tac.toe.data.Mark;

public class EasyComputer extends Computer {
	public EasyComputer(Random rng) {
		super(rng);
	}

	@Override
	public List<Integer> getMoves(Board board, Mark mark) {
		return Stream
				.iterate(0, i -> i + 1)
				.limit(Board.SIZE)
				.filter(board::canMark)
				.toList();
	}
}
