package com.revolut.task.service;


import com.revolut.task.exception.NotFoundException;
import com.revolut.task.exception.SystemException;
import com.revolut.task.model.Account;
import com.revolut.task.repository.AccountDao;
import com.revolut.task.repository.DefaultTransactionManager;
import com.revolut.task.repository.TransactionManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class AccountServiceTest {

    private TransactionManager transactionManager = new TransactionManagerStub();
    private AccountDao accountDao = Mockito.mock(AccountDao.class);
    private AccountService accountService = new AccountService(accountDao, transactionManager);

    @Before
    public void setup() {
        Mockito.reset(accountDao);
    }

    @Test
    public void createAccountSuccessfully() throws Exception {
        when(accountDao.create(any(Account.class), any(Connection.class)))
                .thenReturn(new Account(1L, BigDecimal.ZERO));

        Assert.assertNotNull(accountService.createAccount());
    }

    @Test(expected = SystemException.class)
    public void createAccountFailedThenThrowSystemException() throws Exception {
        when(accountDao.create(any(Account.class), any(Connection.class)))
                .thenThrow(new SQLException());

        accountService.createAccount();
    }

    @Test
    public void findAccountSuccessfully() throws Exception {
        when(accountDao.find(eq(1L), any(Connection.class)))
                .thenReturn(new Account(1L, BigDecimal.ZERO));

        Assert.assertNotNull(accountService.findAccount(1L));
    }

    @Test(expected = NotFoundException.class)
    public void accountNotFoundThenThrowNotFoundException() throws Exception {
        when(accountDao.find(any(Long.class), any(Connection.class)))
                .thenReturn(null);

        accountService.findAccount(1L);
    }
}
