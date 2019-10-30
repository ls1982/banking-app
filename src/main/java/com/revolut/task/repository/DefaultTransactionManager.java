package com.revolut.task.repository;

import com.revolut.task.exception.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Provides default logic transactional wrappers
 *
 * @author Alexey Smirnov
 */
public class DefaultTransactionManager implements TransactionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTransactionManager.class);

    /**
     * Performs amount of work inside a transaction
     *
     * @param work work that should be done
     * @return result of work
     */
    @Override
    public <R> R execute(TransactionalWork<R> work) {
        try (Connection connection = DataSource.getConnection()) {
            final boolean autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            final R result = work.doWork(connection);

            connection.commit();
            connection.setAutoCommit(autoCommit);

            return result;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new SystemException();
        }
    }

    /**
     * Performs amount of work inside a read-only transaction
     *
     * @param work work that should be done
     * @return result of work
     */
    @Override
    public <R> R executeReadOnly(TransactionalWork<R> work) {
        try (Connection connection = DataSource.getConnection()) {
            final boolean readOnly = connection.isReadOnly();
            connection.setReadOnly(true);

            final R result = work.doWork(connection);

            connection.setReadOnly(readOnly);
            return result;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new SystemException();
        }
    }
}
