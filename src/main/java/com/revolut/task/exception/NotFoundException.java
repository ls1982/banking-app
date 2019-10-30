package com.revolut.task.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/** Fires if some object was not found */
public class NotFoundException extends WebApplicationException {
    private static final String DEFAULT_MESSAGE = "%s id = [%d] not found";

    public NotFoundException(String entityName, long id) {
        this(String.format(DEFAULT_MESSAGE, entityName, id));
    }

    public NotFoundException(String message) {
        super(Response.status(Response.Status.NOT_FOUND).
                entity(message).type("text/plain").build());
    }
}