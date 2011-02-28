package ca.wasabistudio.chat.rs;

import java.io.Serializable;

/**
 * RESTful service response status.
 *
 * @author wasabi
 */
@SuppressWarnings("serial")
public final class Status implements Serializable {

    public static final String OK = "ok";
    public static final String ERROR = "error";

    private final String code;
    private final String message;

    public Status(String code) {
        this.code = code;
        this.message = "";
    }

    public Status(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
