package com.revolut.task.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/** Fires if invalid parameters were passed */
public class InvalidParametersException extends WebApplicationException {

    public InvalidParametersException(String message) {
        super(Response.status(Response.Status.BAD_REQUEST).
                entity(message).type("text/plain").build());
    }

}
