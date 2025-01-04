package tic.tac.toe.player;

import java.util.Random;
import java.util.stream.Stream;
import tic.tac.toe.data.Board;
import tic.tac.toe.data.Mark;

public class EasyComputer implements Player {

	private Random rng;

	public EasyComputer(Random rng) {
		this.rng = rng;
	}

	@Override
	public int getMove(Board board, Mark mark) {
		var moves = Stream
				.iterate(0, i -> i + 1)
				.limit(Board.SIZE)
				.filter(board::canMark)
				.toList();
		return moves.get(rng.nextInt(moves.size()));
	}
}
