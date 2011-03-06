package ca.wasabistudio.chat.support;

@SuppressWarnings("serial")
public class SessionExpiredException extends SessionException {

    public SessionExpiredException() {
    }

    public SessionExpiredException(Throwable cause) {
        super(cause);
    }

}
