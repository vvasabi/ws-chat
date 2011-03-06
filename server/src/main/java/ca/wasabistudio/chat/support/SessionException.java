package ca.wasabistudio.chat.support;

public class SessionException extends RuntimeException {

    private static final long serialVersionUID = 6356963603633724506L;

    public SessionException() {
    }

    public SessionException(String message) {
        super(message);
    }

    public SessionException(Throwable cause) {
        super(cause);
    }

}
