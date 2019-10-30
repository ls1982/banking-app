package com.revolut.task.util;

import com.revolut.task.exception.InvalidParametersException;
import com.revolut.task.exception.NotFoundException;

import java.math.BigDecimal;

public final class Assert {

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new InvalidParametersException(message);
        }
    }

    public static void positive(BigDecimal num, String message) {
        if (num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidParametersException(message);
        }
    }

    public static void found(Object object, String entityName, long id) {
        if (object == null) {
            throw new NotFoundException(entityName, id);
        }
    }

    private Assert() {
        // not instantiable
    }
}
