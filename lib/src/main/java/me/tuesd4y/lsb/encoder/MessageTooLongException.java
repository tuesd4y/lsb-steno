package me.tuesd4y.lsb.encoder;

public class MessageTooLongException extends Exception {
    public MessageTooLongException() {
        super();
    }

    MessageTooLongException(String message) {
        super(message);
    }
}
