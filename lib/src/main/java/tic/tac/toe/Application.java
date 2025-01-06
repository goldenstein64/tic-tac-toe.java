package tic.tac.toe;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import tic.tac.toe.data.Board;
import tic.tac.toe.data.Connection;
import tic.tac.toe.data.Mark;
import static tic.tac.toe.data.Message.*;
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

    private static record EndedResult(Optional<Mark> winner) {
    }

    public Application(Connection conn) {
        this.conn = conn;
        this.board = new Board();
    }

    public Application(Connection conn, String pattern) {
        this.conn = conn;
        this.board = new Board(pattern);
    }

    private Computer chooseComputerOnce(Mark mark) throws MessageException {
        var userInput = conn.prompt(new MSG_PromptComputer(mark));
        return switch (userInput) {
            case "E" -> new EasyComputer(new Random());
            case "M" -> new MediumComputer(new Random());
            case "H" -> new HardComputer(new Random());
            default -> throw new MessageException(new ERR_ComputerInvalid(userInput));
        };
    }

    public Player choosePlayerOnce(Mark mark) throws MessageException {
        var userInput = conn.prompt(new MSG_PromptPlayer(mark));
        return switch (userInput) {
            case "H" -> new Human(conn);
            case "C" -> chooseComputerOnce(mark);
            default -> throw new MessageException(new ERR_PlayerInvalid(userInput));
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

    public List<Player> choosePlayers() {
        return Arrays.stream(Mark.values()).map(this::choosePlayer).toList();
    }

    private Optional<EndedResult> playTurn(Player player, Mark mark) {
        var move = player.getMove(board, mark);
        board.set(move, mark);
        return board.won(mark) ? Optional.of(new EndedResult(Optional.of(mark)))
                : board.full() ? Optional.of(new EndedResult(Optional.empty()))
                        : Optional.empty();
    }

    public Optional<Mark> playGame(List<Player> players) {
        var currentIndex = 0;
        conn.print(new MSG_Board(board.toString()));
        while (true) {
            var mark = currentIndex % 2 == 0 ? Mark.X : Mark.O;
            var player = players.get(currentIndex);
            var result = playTurn(player, mark);
            conn.print(new MSG_Board(board.toString()));
            if (result.isPresent()) {
                return result.get().winner;
            }
            currentIndex = (currentIndex + 1) % 2;
        }
    }

    public void displayWinner(Optional<Mark> winner) {
        winner.ifPresentOrElse(
                (mark) -> conn.print(new MSG_PlayerWon(mark)),
                () -> conn.print(MSG_Tied));
    }
}
