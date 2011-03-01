package ca.wasabistudio.chat.support;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class SessionExpiredExceptionMapper
        implements ExceptionMapper<SessionExpiredException> {

    @Override
    public Response toResponse(SessionExpiredException exception) {
        return Response.status(Response.Status.FORBIDDEN).build();
    }

}
