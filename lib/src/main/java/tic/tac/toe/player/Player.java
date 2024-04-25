package tic.tac.toe.player;

import tic.tac.toe.data.Board;
import tic.tac.toe.data.Mark;

public interface Player {
  public int getMove(Board board, Mark mark);
}
