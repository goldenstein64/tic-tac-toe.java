package src.data;

public enum Mark {
  X("X"),
  O("O");

  String ascii;

  Mark(String ascii) {
    this.ascii = ascii;
  }

  public Mark other() {
    switch (this) {
      case X:
        return O;
      case O:
        return X;
      default:
        throw new RuntimeException("Mark is not recognized!");
    }
  }
}
