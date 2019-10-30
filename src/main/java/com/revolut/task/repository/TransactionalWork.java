package com.revolut.task.repository;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Unit of work, that should be done inside a transaction
 */
public interface TransactionalWork<R> {
	R doWork(Connection connection) throws SQLException;
}
