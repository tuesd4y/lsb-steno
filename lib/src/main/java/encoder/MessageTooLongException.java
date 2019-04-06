package encoder;

public class MessageTooLongException extends Exception {
    public MessageTooLongException() {
        super();
    }

    MessageTooLongException(String message) {
        super(message);
    }
}
