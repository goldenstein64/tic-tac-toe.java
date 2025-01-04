package tic.tac.toe;

import java.util.Random;

import tic.tac.toe.data.Board;
import tic.tac.toe.data.Connection;
import tic.tac.toe.data.Mark;
import tic.tac.toe.data.Message;
import tic.tac.toe.data.MessageException;
import tic.tac.toe.player.Computer;
import tic.tac.toe.player.EasyComputer;
import tic.tac.toe.player.HardComputer;
import tic.tac.toe.player.Human;
import tic.tac.toe.player.MediumComputer;
import tic.tac.toe.player.Player;

public class Application {
    private Connection conn;
    private Board board;

    public Application(Connection conn) {
        this.conn = conn;
        this.board = new Board();
    }

    public Application(Connection conn, String pattern) {
        this.conn = conn;
        this.board = new Board(pattern);
    }

    private Computer chooseComputerOnce(Mark mark) throws MessageException {
        var userInput = conn.prompt(new Message.MSG_PromptComputer(mark));
        return switch (userInput) {
            case "E" -> new EasyComputer(new Random());
            case "M" -> new MediumComputer(new Random());
            case "H" -> new HardComputer(new Random());
            default -> throw new MessageException(new Message.ERR_ComputerInvalid(userInput));
        };
    }

    public Player choosePlayerOnce(Mark mark) throws MessageException {
        var userInput = conn.prompt(new Message.MSG_PromptPlayer(mark));
        return switch (userInput) {
            case "H" -> new Human(conn);
            case "C" -> chooseComputerOnce(mark);
            default -> throw new MessageException(new Message.ERR_PlayerInvalid(userInput));
        };
    }

    public Player choosePlayer(Mark mark) {
        while (true) {
            try {
                return choosePlayerOnce(mark);
            } catch (MessageException exception) {
                conn.print(exception.message);
            }
        }
    }
}
