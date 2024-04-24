package src.game.board;

/**
 * An enum that represents every possible value each slot in a tic-tac-toe game
 * can be.
 */
public enum Mark {
  X("X"),
  O("O");

  private String ascii;

  private Mark(String ascii) {
    this.ascii = ascii;
  }

  public String toString() {
    return ascii;
  }

  public Mark other() {
    switch (this) {
      case O:
        return X;
      case X:
        return O;
      default:
        throw new RuntimeException("unknown mark");
    }
  }
}
