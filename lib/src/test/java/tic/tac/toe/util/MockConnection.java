package tic.tac.toe.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import tic.tac.toe.data.Connection;
import tic.tac.toe.data.Message;

public class MockConnection implements Connection {

  public Queue<String> inputs;
  public List<Message> outputs = new ArrayList<Message>();

  public MockConnection(Collection<String> inputs) {
    this.inputs = new ArrayDeque<String>(inputs);
  }

  public MockConnection(String... inputs) {
    this.inputs = new ArrayDeque<String>(List.of(inputs));
  }

  @Override
  public void print(Message message, Object... args) {
    outputs.add(message);
  }

  @Override
  public String prompt(Message message, Object... args) {
    outputs.add(message);
    return inputs.remove();
  }
}
