package ca.wasabistudio.chat.support;

@SuppressWarnings("serial")
public class RequestErrorException extends RuntimeException {

    public RequestErrorException(String message) {
        super(message);
    }

}
