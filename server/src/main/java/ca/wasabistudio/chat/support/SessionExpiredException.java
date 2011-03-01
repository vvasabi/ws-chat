package ca.wasabistudio.chat.support;

@SuppressWarnings("serial")
public class SessionExpiredException extends RuntimeException {

    public SessionExpiredException() {
    }

    public SessionExpiredException(Throwable cause) {
        super(cause);
    }

}
