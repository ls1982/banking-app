package com.revolut.task.repository;

import com.revolut.task.model.Operation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OperationDao {

    private static final String OPERATION_INSERT_QUERY = "INSERT INTO operation(account_from, account_to, type, amount) VALUES (?, ?, ?, ?)";
    private static final String OPERATION_GET_BY_ID_QUERY = "SELECT * FROM operation WHERE id = ?";
    private static final String OPERATION_GET_ALL_BY_ACCOUNT_NUMBER_QUERY = "SELECT * FROM operation WHERE account_from = ? OR account_to = ?";

    public Operation save(Operation operation, Connection connection) throws SQLException {
        final long id = JdbcQueryExecutor.with(connection)
                .executeInsert(OPERATION_INSERT_QUERY,
                        Stream.of(operation.getAccountFrom(),
                                operation.getAccountTo(),
                                operation.getType().name(),
                                operation.getAmount())
                                .collect(Collectors.toList()));

        return new Operation(id, operation.getAccountFrom(),
                operation.getAccountTo(), operation.getType(),
                operation.getAmount());
    }

    public Operation find(long operationId, Connection connection) throws SQLException {
        return JdbcQueryExecutor.with(connection).selectOne(OPERATION_GET_BY_ID_QUERY,
                Collections.singletonList(operationId),
                operationRowMapper);
    }

    public List<Operation> findAll(long accountNumber, Connection connection) throws SQLException {
        return JdbcQueryExecutor.with(connection).select(OPERATION_GET_ALL_BY_ACCOUNT_NUMBER_QUERY,
                Stream.of(accountNumber, accountNumber).collect(Collectors.toList()),
                operationRowMapper);
    }

    private RowMapper<Operation> operationRowMapper = resultSet -> {
        Long accountFrom = resultSet.getLong("account_from");
        if (resultSet.wasNull()) {
            accountFrom = null;
        }

        Long accountTo = resultSet.getLong("account_to");
        if (resultSet.wasNull()) {
            accountTo = null;
        }

        return new Operation(resultSet.getLong("id"),
                accountFrom,
                accountTo,
                Operation.Type.valueOf(resultSet.getString("type")),
                resultSet.getBigDecimal("amount"));
    };

}
