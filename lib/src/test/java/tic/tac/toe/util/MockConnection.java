package tic.tac.toe.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import tic.tac.toe.data.Connection;
import tic.tac.toe.data.Message;

public class MockConnection implements Connection {

  List<String> inputs;
  List<Message> outputs = new ArrayList<Message>();

  public MockConnection(Collection<String> inputs) {
    this.inputs = new ArrayList<String>(inputs);
  }

  @Override
  public void print(Message message, Object... args) {
    outputs.add(message);
  }

  @Override
  public String prompt(Message message, Object... args) {
    outputs.add(message);
    return inputs.remove(inputs.size() - 1);
  }
}
