package tic.tac.toe;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.Test;

import tic.tac.toe.data.Mark;
import tic.tac.toe.data.Message.*;
import tic.tac.toe.data.MessageException;
import tic.tac.toe.player.EasyComputer;
import tic.tac.toe.player.HardComputer;
import tic.tac.toe.player.Human;
import tic.tac.toe.player.MediumComputer;
import tic.tac.toe.util.MockConnection;

public class ApplicationTest {
    public static class ChoosePlayerOnceTest {
        @Test
        public void returnsHardComputerOnCH() throws MessageException {
            var conn = new MockConnection("C", "H");
            var app = new Application(conn);
            var chosenPlayer = app.choosePlayerOnce(Mark.X);

            assertInstanceOf(HardComputer.class, chosenPlayer);
            assertEquals(List.of(
                    new MSG_PromptPlayer(Mark.X),
                    new MSG_PromptComputer(Mark.X)),
                    conn.outputs);
        }

        @Test
        public void returnsMediumComputerOnCM() throws MessageException {
            var conn = new MockConnection("C", "M");
            var app = new Application(conn);
            var chosenPlayer = app.choosePlayerOnce(Mark.X);

            assertInstanceOf(MediumComputer.class, chosenPlayer);
            assertEquals(List.of(
                    new MSG_PromptPlayer(Mark.X),
                    new MSG_PromptComputer(Mark.X)),
                    conn.outputs);
        }

        @Test
        public void returnsEasyComputerOnCE() throws MessageException {
            var conn = new MockConnection("C", "E");
            var app = new Application(conn);
            var chosenPlayer = app.choosePlayerOnce(Mark.X);

            assertInstanceOf(EasyComputer.class, chosenPlayer);
            assertEquals(List.of(
                    new MSG_PromptPlayer(Mark.X),
                    new MSG_PromptComputer(Mark.X)),
                    conn.outputs);
        }

        @Test
        public void returnsHumanOnH() throws MessageException {
            var conn = new MockConnection("H");
            var app = new Application(conn);
            var chosenPlayer = app.choosePlayerOnce(Mark.X);

            assertInstanceOf(Human.class, chosenPlayer);
            assertEquals(List.of(new MSG_PromptPlayer(Mark.X)), conn.outputs);
        }

        @Test
        public void throwsOnInvalidComputer() {
            var conn = new MockConnection("C", "@");
            var app = new Application(conn);
            var exception = assertThrows(
                    MessageException.class,
                    () -> app.choosePlayerOnce(Mark.X));

            assertEquals(exception.message, new ERR_ComputerInvalid("@"));
            assertEquals(List.of(
                    new MSG_PromptPlayer(Mark.X),
                    new MSG_PromptComputer(Mark.X)),
                    conn.outputs);
        }

        @Test
        public void throwsOnInvalidPlayer() {
            var conn = new MockConnection("@");
            var app = new Application(conn);
            var exception = assertThrows(
                    MessageException.class,
                    () -> app.choosePlayerOnce(Mark.X));

            assertEquals(exception.message, new ERR_PlayerInvalid("@"));
            assertEquals(List.of(new MSG_PromptPlayer(Mark.X)), conn.outputs);
        }
    }

    public static class ChoosePlayerTest {
        @Test
        public void retriesOnInvalidPlayer() {
            var conn = new MockConnection("#", "H");
            var app = new Application(conn);
            var chosenPlayer = app.choosePlayer(Mark.X);

            assertInstanceOf(Human.class, chosenPlayer);
            assertEquals(List.of(
                    new MSG_PromptPlayer(Mark.X),
                    new ERR_PlayerInvalid("#"),
                    new MSG_PromptPlayer(Mark.X)),
                    conn.outputs);
        }

        @Test
        public void retriesOnInvalidComputer() {
            var conn = new MockConnection("C", "@", "H");
            var app = new Application(conn);
            var chosenPlayer = app.choosePlayer(Mark.X);

            assertInstanceOf(Human.class, chosenPlayer);
            assertEquals(List.of(
                    new MSG_PromptPlayer(Mark.X),
                    new MSG_PromptComputer(Mark.X),
                    new ERR_ComputerInvalid("@"),
                    new MSG_PromptPlayer(Mark.X)),
                    conn.outputs);
        }
    }

    public static class ChoosePlayersTest {
        @Test
        public void retriesOnInvalidPlayer() {

            var conn = new MockConnection("@", "C", "H", "#", "$", "H");
            var app = new Application(conn);

            var players = app.choosePlayers();

            assertEquals(
                    List.of(
                            new MSG_PromptPlayer(Mark.X),
                            new ERR_PlayerInvalid("@"),
                            new MSG_PromptPlayer(Mark.X),
                            new MSG_PromptComputer(Mark.X),
                            new MSG_PromptPlayer(Mark.O),
                            new ERR_PlayerInvalid("#"),
                            new MSG_PromptPlayer(Mark.O),
                            new ERR_PlayerInvalid("$"),
                            new MSG_PromptPlayer(Mark.O)),
                    conn.outputs);

            assertEquals(2, players.size());
            assertInstanceOf(HardComputer.class, players.get(0));
            assertInstanceOf(Human.class, players.get(1));
        }

        @Test
        public void retriesOnInvalidComputer() {
            var conn = new MockConnection("C", "@", "C", "M", "#", "C", "$", "C", "E");
            var app = new Application(conn);

            var players = app.choosePlayers();

            assertEquals(
                    List.of(
                            new MSG_PromptPlayer(Mark.X), // "C"
                            new MSG_PromptComputer(Mark.X), // "@"
                            new ERR_ComputerInvalid("@"),
                            new MSG_PromptPlayer(Mark.X), // "C"
                            new MSG_PromptComputer(Mark.X), // "M"
                            new MSG_PromptPlayer(Mark.O), // "@"
                            new ERR_PlayerInvalid("#"),
                            new MSG_PromptPlayer(Mark.O), // "C"
                            new MSG_PromptComputer(Mark.O), // "@"
                            new ERR_ComputerInvalid("$"),
                            new MSG_PromptPlayer(Mark.O), // "C"
                            new MSG_PromptComputer(Mark.O) // "E"
                    ),
                    conn.outputs);

            assertEquals(2, players.size());
            assertInstanceOf(MediumComputer.class, players.get(0));
            assertInstanceOf(EasyComputer.class, players.get(1));
        }
    }

    public static class DisplayWinnerTest {
        @Test
        public void tieOnEmpty() {
            var conn = new MockConnection();
            var app = new Application(conn);

            app.displayWinner(Optional.empty());
            assertEquals(List.of(MSG_Tied.INSTANCE), conn.outputs);
        }

        @Test
        public void winXOnMarkX() {
            var conn = new MockConnection();
            var app = new Application(conn);

            app.displayWinner(Optional.of(Mark.X));
            assertEquals(List.of(new MSG_PlayerWon(Mark.X)), conn.outputs);
        }

        @Test
        public void winOOnMarkO() {
            var conn = new MockConnection();
            var app = new Application(conn);

            app.displayWinner(Optional.of(Mark.O));
            assertEquals(List.of(new MSG_PlayerWon(Mark.O)), conn.outputs);
        }
    }

    public static class PlayGameTest {
        @Test
        public void canRunBetweenHumans() {
            var conn = new MockConnection("1", "2", "7", "4", "9", "5", "8");

            var app = new Application(conn);
            var human1 = new Human(conn);
            var human2 = new Human(conn);
            var winner = app.playGame(List.of(human1, human2));

            assertEquals(Optional.of(Mark.X), winner);
            assertEquals(
                    List.of(
                            new MSG_Board(",,,,,,,,,"),
                            new MSG_PromptMove(Mark.X), // "1"
                            new MSG_Board("X,,,,,,,,"),
                            new MSG_PromptMove(Mark.O), // "2"
                            new MSG_Board("XO,,,,,,,"),
                            new MSG_PromptMove(Mark.X), // "7"
                            new MSG_Board("XO,,,,X,,"),
                            new MSG_PromptMove(Mark.O), // "4"
                            new MSG_Board("XO,O,,X,,"),
                            new MSG_PromptMove(Mark.X), // "9"
                            new MSG_Board("XO,O,,X,X"),
                            new MSG_PromptMove(Mark.O), // "5"
                            new MSG_Board("XO,OO,X,X"),
                            new MSG_PromptMove(Mark.X), // "8"
                            new MSG_Board("XO,OO,XXX")),
                    conn.outputs);
        }

        @Test
        public void canRunBetweenComputers() {
            var conn = new MockConnection();
            var app = new Application(conn);

            var winner = app.playGame(List.of(new MediumComputer(new Random()), new MediumComputer(new Random())));

            assertTrue(conn.outputs.contains(new MSG_Board(",,,,,,,,,")));
        }
    }
}
