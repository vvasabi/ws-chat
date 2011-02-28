package ca.wasabistudio.chat.rs;

@SuppressWarnings("serial")
public class RequestErrorException extends RuntimeException {

    public RequestErrorException(String message) {
        super(message);
    }

}
