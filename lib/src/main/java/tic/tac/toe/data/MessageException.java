package tic.tac.toe.data;

public class MessageException extends Exception {
    public final Message message;

    public MessageException(Message message) {
        super();
        this.message = message;
    }
}