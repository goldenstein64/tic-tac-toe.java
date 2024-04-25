package tic.tac.toe.data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MarkTest {

  @Test
  void toStringWorks() {
    assertEquals("X", Mark.X.toString());
    assertEquals("O", Mark.O.toString());
  }

  @Test
  void otherWorks() {
    assertEquals(Mark.O, Mark.X.other());
    assertEquals(Mark.X, Mark.O.other());
  }
}
