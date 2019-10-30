package com.revolut.task.service;


import com.revolut.task.exception.InsufficientFundsException;
import com.revolut.task.exception.InvalidParametersException;
import com.revolut.task.exception.NotFoundException;
import com.revolut.task.exception.SystemException;
import com.revolut.task.model.Account;
import com.revolut.task.model.Operation;
import com.revolut.task.repository.AccountDao;
import com.revolut.task.repository.OperationDao;
import com.revolut.task.repository.TransactionManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class OperationServiceTest {
	private TransactionManager transactionManager = new TransactionManagerStub();
	private AccountDao accountDao = Mockito.mock(AccountDao.class);
	private OperationDao operationDao = Mockito.mock(OperationDao.class);
	private OperationService operationService = new OperationService(accountDao, operationDao, transactionManager);

	@Before
	public void setup() {
		Mockito.reset(accountDao, operationDao);
	}

	@Test(expected = NotFoundException.class)
	public void depositAccountNotFountThenThrowException() throws Exception {
		when(accountDao.find(any(Long.class), any(Connection.class)))
				.thenReturn(null);

		operationService.deposit(1L, BigDecimal.TEN);
	}

	@Test(expected = SystemException.class)
	public void depositAccountDatabaseErrorThenThrowSystemException() throws Exception {
		when(accountDao.find(any(Long.class), any(Connection.class)))
				.thenReturn(new Account(1L, BigDecimal.ZERO));

		doThrow(new SQLException()).when(accountDao).update(any(Account.class), any(Connection.class));

		operationService.deposit(1L, BigDecimal.TEN);
	}

	@Test(expected = SystemException.class)
	public void depositAccountOperationDatabaseErrorThenThrowSystemException() throws Exception {
		when(accountDao.find(any(Long.class), any(Connection.class)))
				.thenReturn(new Account(1L, BigDecimal.ZERO));

		doThrow(new SQLException()).when(operationDao).save(any(Operation.class), any(Connection.class));

		operationService.deposit(1L, BigDecimal.TEN);
	}

	@Test(expected = NotFoundException.class)
	public void withdrawAccountNotFountThenThrowException() throws Exception {
		when(accountDao.find(any(Long.class), any(Connection.class)))
				.thenReturn(null);

		operationService.withdraw(1L, BigDecimal.TEN);
	}

	@Test(expected = SystemException.class)
	public void withdrawAccountDatabaseErrorThenThrowSystemException() throws Exception {
		when(accountDao.find(any(Long.class), any(Connection.class)))
				.thenReturn(new Account(1L, BigDecimal.TEN));

		doThrow(new SQLException()).when(accountDao).update(any(Account.class), any(Connection.class));

		operationService.withdraw(1L, BigDecimal.ONE);
	}

	@Test(expected = SystemException.class)
	public void withdrawDatabaseErrorThenThrowException() throws Exception {
		when(accountDao.find(any(Long.class), any(Connection.class)))
				.thenReturn(new Account(1L, BigDecimal.TEN));

		doThrow(new SQLException()).when(operationDao).save(any(Operation.class), any(Connection.class));

		operationService.withdraw(1L, BigDecimal.ONE);
	}

	@Test(expected = InsufficientFundsException.class)
	public void withdrawNoMoneyThenThrowException() throws Exception {
		when(accountDao.find(any(Long.class), any(Connection.class)))
				.thenReturn(new Account(1L, BigDecimal.ZERO));

		doThrow(new SQLException()).when(operationDao).save(any(Operation.class), any(Connection.class));

		operationService.withdraw(1L, BigDecimal.TEN);
	}

	@Test(expected = InvalidParametersException.class)
	public void transferAmountIsZeroThenThrowException() throws Exception {
		operationService.transfer(1L, 2L, BigDecimal.ZERO);
	}

	@Test(expected = InvalidParametersException.class)
	public void transferAccountFromNotSpecifiedThenThrowException() throws Exception {
		operationService.transfer(null, 2L, BigDecimal.ZERO);
	}

	@Test(expected = InvalidParametersException.class)
	public void transferAccountToNotSpecifiedThenThrowException() throws Exception {
		operationService.transfer(1L, null, BigDecimal.ZERO);
	}

	@Test(expected = InvalidParametersException.class)
	public void transferSelfThenThrowException() throws Exception {
		operationService.transfer(1L, 1L, BigDecimal.ZERO);
	}

	@Test(expected = InsufficientFundsException.class)
	public void transterAccountFromNoMoneyThenThrowException() throws Exception {
		when(accountDao.find(eq(1L), any(Connection.class)))
				.thenReturn(new Account(1L, BigDecimal.ZERO));

		when(accountDao.find(eq(2L), any(Connection.class)))
				.thenReturn(new Account(2L, BigDecimal.ZERO));

		operationService.transfer(1L, 2L, BigDecimal.TEN);
	}

	@Test(expected = SystemException.class)
	public void transferDatabaseErrorThenThrowException() throws Exception {
		when(accountDao.find(eq(1L), any(Connection.class)))
				.thenReturn(new Account(1L, BigDecimal.TEN));

		when(accountDao.find(eq(2L), any(Connection.class)))
				.thenReturn(new Account(2L, BigDecimal.ZERO));

		doThrow(new SQLException()).when(accountDao).update(any(Account.class), any(Connection.class));

		operationService.transfer(1L, 2L, BigDecimal.ONE);
	}

	@Test(expected = SystemException.class)
	public void transferOperationDatabaseErrorThenThrowException() throws Exception {
		when(accountDao.find(eq(1L), any(Connection.class)))
				.thenReturn(new Account(1L, BigDecimal.TEN));

		when(accountDao.find(eq(2L), any(Connection.class)))
				.thenReturn(new Account(2L, BigDecimal.ZERO));

		doThrow(new SQLException()).when(operationDao).save(any(Operation.class), any(Connection.class));

		operationService.transfer(1L, 2L, BigDecimal.ONE);
	}
}
