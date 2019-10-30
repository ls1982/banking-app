package com.revolut.task.repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Executes queries using JDBC connection
 *
 * @author Alexey Smirnov
 */
public final class JdbcQueryExecutor {

	private Connection connection;

	public static JdbcQueryExecutor with(Connection connection) {
		return new JdbcQueryExecutor(connection);
	}

	private JdbcQueryExecutor(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Executes an <tt>INSERT</tt> query
	 *
	 * @param query query text
	 * @param params list of params. Named params are not supported
	 */
	public Long executeInsert(String query, List<?> params) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(query,
				Statement.RETURN_GENERATED_KEYS)) {

			fillStatementWithParams(statement, params);

			int affectedRows = statement.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Insertion failed, no rows affected.");
			}

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return generatedKeys.getLong(1);
				} else {
					throw new SQLException("Insertion failed, no ID obtained.");
				}
			}
		}
	}

	/**
	 * Executes an <tt>UPDATE</tt> query
	 *
	 * @param query query text
	 * @param params list of params. Named params are not supported
	 */
	public void executeUpdate(String query, List<?> params) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(query)) {

			fillStatementWithParams(statement, params);

			int affectedRows = statement.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Updating account failed, no rows affected.");
			}
		}
	}

	/**
	 * Executes an <tt>SELECT</tt> query
	 *
	 * @param query query text
	 * @param params list of params. Named params are not supported
	 * @param rowMapper row mapper. See {@link RowMapper}
	 *
	 * @return the first result record
	 */
	public <T> T selectOne(String query, List<?> params, RowMapper<T> rowMapper) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(query)) {

			fillStatementWithParams(statement, params);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return rowMapper.map(resultSet);
				}
			}

			return null;
		}
	}

	/**
	 * Executes an <tt>SELECT</tt> query
	 *
	 * @param query query text
	 * @param params list of params. Named params are not supported
	 * @param rowMapper row mapper. See {@link RowMapper}
	 *
	 * @return all records found
	 */
	public <T> List<T> select(String query, List<?> params, RowMapper<T> rowMapper) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(query)) {

			fillStatementWithParams(statement, params);

			try (ResultSet resultSet = statement.executeQuery()) {
				final List<T> operations = new ArrayList<>();
				while (resultSet.next()) {
					operations.add(rowMapper.map(resultSet));
				}

				return operations;
			}
		}
	}

	private void fillStatementWithParams(PreparedStatement statement, List<?> params) throws SQLException {
		for (int i = 1; i <= params.size(); i++) {
			Object param = params.get(i - 1);

			if (param == null) {
				statement.setNull(i, Types.NULL);
			} else {
				if (param instanceof Long) {
					statement.setLong(i, (Long) param);
				} else if (param instanceof BigDecimal) {
					statement.setBigDecimal(i, (BigDecimal) param);
				} else if (param instanceof String) {
					statement.setString(i, (String) param);
				} else {
					throw new SQLException("Unsupported parameter type:" + param.getClass().getSimpleName());
				}
			}
		}
	}

	private JdbcQueryExecutor() {
		//not instantiable
	}

}
