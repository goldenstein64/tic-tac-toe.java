package src.data;

public interface Connection {
  public String prompt(Message message, Object... args);

  public void print(Message message, Object... args);
}
