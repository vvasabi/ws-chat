package ca.wasabistudio.chat.support;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class SessionExceptionMapper
        implements ExceptionMapper<SessionException> {

    @Override
    public Response toResponse(SessionException exception) {
        return Response.status(Response.Status.FORBIDDEN).build();
    }

}
