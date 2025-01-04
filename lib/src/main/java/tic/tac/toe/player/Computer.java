package tic.tac.toe.player;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import tic.tac.toe.data.Board;
import tic.tac.toe.data.Mark;

public abstract class Computer implements Player {
    protected Random rng;

    protected Computer(Random rng) {
        this.rng = rng;
    }

    protected abstract List<Integer> getMoves(Board board, Mark mark);

    @Override
    public int getMove(Board board, Mark mark) {
        final var moves = getMoves(board, mark);
        if (moves.size() <= 0) {
            throw new NoSuchElementException("no moves to take!");
        }
        return moves.get(rng.nextInt(moves.size()));
    }
}
