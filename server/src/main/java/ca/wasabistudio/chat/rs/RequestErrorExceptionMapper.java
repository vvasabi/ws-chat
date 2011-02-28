package ca.wasabistudio.chat.rs;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class RequestErrorExceptionMapper
    implements ExceptionMapper<RequestErrorException> {

    @Override
    public Response toResponse(RequestErrorException exception) {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}
