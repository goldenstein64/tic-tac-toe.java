package tic.tac.toe;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import tic.tac.toe.data.Mark;
import tic.tac.toe.data.Message;
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
                    new Message.MSG_PromptPlayer(Mark.X),
                    new Message.MSG_PromptComputer(Mark.X)),
                    conn.outputs);
        }

        @Test
        public void returnsMediumComputerOnCM() throws MessageException {
            var conn = new MockConnection("C", "M");
            var app = new Application(conn);
            var chosenPlayer = app.choosePlayerOnce(Mark.X);

            assertInstanceOf(MediumComputer.class, chosenPlayer);
            assertEquals(List.of(
                    new Message.MSG_PromptPlayer(Mark.X),
                    new Message.MSG_PromptComputer(Mark.X)),
                    conn.outputs);
        }

        @Test
        public void returnsEasyComputerOnCE() throws MessageException {
            var conn = new MockConnection("C", "E");
            var app = new Application(conn);
            var chosenPlayer = app.choosePlayerOnce(Mark.X);

            assertInstanceOf(EasyComputer.class, chosenPlayer);
            assertEquals(List.of(
                    new Message.MSG_PromptPlayer(Mark.X),
                    new Message.MSG_PromptComputer(Mark.X)),
                    conn.outputs);
        }

        @Test
        public void returnsHumanOnH() throws MessageException {
            var conn = new MockConnection("H");
            var app = new Application(conn);
            var chosenPlayer = app.choosePlayerOnce(Mark.X);

            assertInstanceOf(Human.class, chosenPlayer);
            assertEquals(List.of(new Message.MSG_PromptPlayer(Mark.X)), conn.outputs);
        }

        @Test
        public void throwsOnInvalidComputer() {
            var conn = new MockConnection("C", "@");
            var app = new Application(conn);
            var exception = assertThrows(
                    MessageException.class,
                    () -> app.choosePlayerOnce(Mark.X));

            assertEquals(exception.message, new Message.ERR_ComputerInvalid("@"));
            assertEquals(List.of(
                    new Message.MSG_PromptPlayer(Mark.X),
                    new Message.MSG_PromptComputer(Mark.X)),
                    conn.outputs);
        }

        @Test
        public void throwsOnInvalidPlayer() {
            var conn = new MockConnection("@");
            var app = new Application(conn);
            var exception = assertThrows(
                    MessageException.class,
                    () -> app.choosePlayerOnce(Mark.X));

            assertEquals(exception.message, new Message.ERR_PlayerInvalid("@"));
            assertEquals(List.of(new Message.MSG_PromptPlayer(Mark.X)), conn.outputs);
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
                    new Message.MSG_PromptPlayer(Mark.X),
                    new Message.ERR_PlayerInvalid("#"),
                    new Message.MSG_PromptPlayer(Mark.X)),
                    conn.outputs);
        }

        @Test
        public void retriesOnInvalidComputer() {
            var conn = new MockConnection("C", "@", "H");
            var app = new Application(conn);
            var chosenPlayer = app.choosePlayer(Mark.X);

            assertInstanceOf(Human.class, chosenPlayer);
            assertEquals(List.of(
                    new Message.MSG_PromptPlayer(Mark.X),
                    new Message.MSG_PromptComputer(Mark.X),
                    new Message.ERR_ComputerInvalid("@"),
                    new Message.MSG_PromptPlayer(Mark.X)),
                    conn.outputs);
        }
    }
}
