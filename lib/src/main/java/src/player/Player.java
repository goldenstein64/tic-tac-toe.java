package src.player;

import src.data.Board;
import src.data.Mark;

public interface Player {
  public int getMove(Board board, Mark mark);
}
