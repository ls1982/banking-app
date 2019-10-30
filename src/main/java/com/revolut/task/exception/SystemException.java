package com.revolut.task.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/** Fires if something has happened unexpectedly */
public class SystemException extends WebApplicationException {
    private static final String DEFAULT_MESSAGE = "Internal system error has occurred";

    public SystemException() {
        this(DEFAULT_MESSAGE);
    }

    public SystemException(String message) {
        super(Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                entity(message).type("text/plain").build());
    }
}
