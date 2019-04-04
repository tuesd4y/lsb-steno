package encoder;

public class MessageTooLongException extends Exception {
    public MessageTooLongException() {
        super();
    }

    public MessageTooLongException(String message) {
        super(message);
    }
}
