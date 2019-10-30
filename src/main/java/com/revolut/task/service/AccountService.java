package com.revolut.task.service;

import com.revolut.task.exception.SystemException;
import com.revolut.task.model.Account;
import com.revolut.task.repository.AccountDao;
import com.revolut.task.repository.TransactionManager;
import com.revolut.task.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigDecimal;

/**
 * Contains bank account management business logic.
 * Can be used to create and obtain account current state.
 *
 * @author Alexey Smirnov
 */
public class AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    private AccountDao accountDao;
    private TransactionManager transactionManager;

    @Inject
    public AccountService(AccountDao accountDao, TransactionManager transactionManager) {
        this.accountDao = accountDao;
        this.transactionManager = transactionManager;
    }

    /**
     * Creates a new account
     *
     * @return {@link Account} created account
     */
    public Account createAccount() throws SystemException {
        final Account account = transactionManager.execute(connection ->
                accountDao.create(new Account(null, BigDecimal.ZERO), connection));

        LOGGER.info("Account [{}] has been created", account.getAccountNumber());

        return account;
    }

    /**
     * Finds an account by the account number
     *
     * @param accountNumber account number to search
     * @return {@link Account} account found
     */
    public Account findAccount(final Long accountNumber) throws SystemException {
        final Account account = transactionManager.executeReadOnly(connection ->
                accountDao.find(accountNumber, connection));

        Assert.found(account, "Account", accountNumber);

        return account;
    }
}
