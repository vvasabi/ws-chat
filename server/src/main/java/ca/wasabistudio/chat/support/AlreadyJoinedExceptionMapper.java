package ca.wasabistudio.chat.support;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class AlreadyJoinedExceptionMapper
        implements ExceptionMapper<AlreadyJoinedException> {

    @Override
    public Response toResponse(AlreadyJoinedException exception) {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}
