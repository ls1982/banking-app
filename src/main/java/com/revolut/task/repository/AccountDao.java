package com.revolut.task.repository;

import com.revolut.task.model.Account;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Data access object to perform CRUD operations with accounts
 *
 * @author Alexey Smirnov
 */
public class AccountDao {

	private static final String ACCOUNT_INSERT_QUERY = "INSERT INTO account(balance) VALUES (?)";
	private static final String ACCOUNT_UPDATE_QUERY = "UPDATE account SET balance = ? WHERE account_number = ?";
	private static final String ACCOUNT_FIND_BY_BY_ACCOUNT_NUMBER_QUERY = "SELECT * FROM account WHERE account_number = ?";

	/**
	 * Retrieves account from database
	 *
	 * @param accountNumber account number
	 * @param connection    Connection to database
	 *
	 * @return Account or {@literal null} if account not found
	 */
	public Account find(long accountNumber, Connection connection) throws SQLException {
		return JdbcQueryExecutor.with(connection).selectOne(ACCOUNT_FIND_BY_BY_ACCOUNT_NUMBER_QUERY,
				Collections.singletonList(accountNumber), accountRowMapper);
	}

	/**
	 * Creates new account record in database
	 *
	 * @param account account object to persist
	 * @param connection    Connection to database
	 *
	 * @return Account with corresponding account number
	 */
	public Account create(Account account, Connection connection) throws SQLException {
		final Long id = JdbcQueryExecutor.with(connection)
				.executeInsert(ACCOUNT_INSERT_QUERY, Collections.singletonList(account.getBalance()));
		return new Account(id, account.getBalance());
	}

	/**
	 * Updates existing account record in database
	 *
	 * @param account account object to persist
	 * @param connection    Connection to database
	 */
	public void update(Account account, Connection connection) throws SQLException {
		JdbcQueryExecutor.with(connection).executeUpdate(ACCOUNT_UPDATE_QUERY,
				Stream.of(account.getBalance(), account.getAccountNumber())
						.collect(Collectors.toList()));
	}

	private RowMapper<Account> accountRowMapper = resultSet ->
			new Account(resultSet.getLong("account_number"),
					resultSet.getBigDecimal("balance"));
}
