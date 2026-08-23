package io.github.goldenstein64.tic.tac.toe.player;

import io.github.goldenstein64.tic.tac.toe.data.Board;
import io.github.goldenstein64.tic.tac.toe.data.Mark;

public interface Player {
	public int getMove(Board board, Mark mark);
}
