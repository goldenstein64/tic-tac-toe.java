package src;

import java.util.Scanner;

import tic.tac.toe.data.Connection;
import tic.tac.toe.data.Message;

public abstract class ConsoleConnection implements Connection {
    Scanner scanner = new Scanner(System.in);

    public abstract String format(Message message);

    @Override
    public String prompt(Message message) {
        System.out.print(format(message));
        return scanner.nextLine();
    }

    @Override
    public void print(Message message) {
        System.out.println(format(message));
    }

}
