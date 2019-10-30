package com.revolut.task.service;

import com.revolut.task.exception.SystemException;
import com.revolut.task.repository.TransactionManager;
import com.revolut.task.repository.TransactionalWork;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/** Test implementation providing mocked database connection */
public class TransactionManagerStub implements TransactionManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionManagerStub.class);

	@Override
	public <R> R execute(TransactionalWork<R> work) {
		try {
			return work.doWork(Mockito.mock(Connection.class));
		} catch (SQLException e) {
			LOGGER.debug(e.getMessage());
			throw new SystemException();
		}
	}

	@Override
	public <R> R executeReadOnly(TransactionalWork<R> work) {
		try {
			return work.doWork(Mockito.mock(Connection.class));
		} catch (SQLException e) {
			LOGGER.debug(e.getMessage());
			throw new SystemException();
		}
	}
}
