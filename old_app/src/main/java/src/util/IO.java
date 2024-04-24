package src.util;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class IO {

  private Map<String, String> messages = new HashMap<>();
  private static Scanner input = new Scanner(System.in);

  public IO register(String key, String message) {
    messages.put(key, message);
    return this;
  }

  public String prompt(String message, Object... args) {
    message = messages.getOrDefault(message, message);
    System.out.printf(message, args);

    try {
      return input.nextLine();
    } catch (NoSuchElementException e) {
      throw new RuntimeException("Keyboard interrupt");
    }
  }

  public void print(String message, Object... args) {
    message = messages.getOrDefault(message, message);
    System.out.printf(message, args);
    System.out.println();
  }
}
