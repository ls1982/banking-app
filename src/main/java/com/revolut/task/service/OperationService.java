package com.revolut.task.service;

import com.revolut.task.exception.InsufficientFundsException;
import com.revolut.task.exception.InvalidParametersException;
import com.revolut.task.model.Account;
import com.revolut.task.model.Operation;
import com.revolut.task.repository.AccountDao;
import com.revolut.task.repository.OperationDao;
import com.revolut.task.repository.TransactionManager;
import com.revolut.task.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides functionality to perform financial operations
 *
 * @author Alexey Smirnov
 */
public class OperationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationService.class);

    private AccountDao accountDao;
    private OperationDao operationDao;
    private TransactionManager transactionManager;

    @Inject
    public OperationService(AccountDao accountDao, OperationDao operationDao, TransactionManager transactionManager) {
        this.accountDao = accountDao;
        this.operationDao = operationDao;
        this.transactionManager = transactionManager;
    }

    /**
     * Fullfills account with money
     *
     * @param accountNumber number of account to put
     * @param amount        amount of money
     */
    public void deposit(long accountNumber, BigDecimal amount) {
        Assert.notNull(amount, "amount is null");
        Assert.positive(amount, "amount is not positive");

        transactionManager.execute(connection -> {
            final Account account = accountDao.find(accountNumber, connection);
            Assert.found(account, "Account", accountNumber);

            account.setBalance(account.getBalance().add(amount));
            accountDao.update(account, connection);

            final Operation operation = Operation.deposit(accountNumber, amount);
            operationDao.save(operation, connection);

            return null;
        });

        LOGGER.info("Account [{}] has been filled with [{}]", accountNumber, amount);
    }

    /**
     * Withdraws money from account
     *
     * @param accountNumber number of account to put
     * @param amount        amount of money
     */
    public void withdraw(long accountNumber, BigDecimal amount) {
        Assert.notNull(amount, "amount is null");
        Assert.positive(amount, "amount is not positive");

        transactionManager.execute(connection -> {
            final Account account = accountDao.find(accountNumber, connection);
            Assert.found(account, "Account", accountNumber);

            checkBalanceWithdrawal(accountNumber, account.getBalance(), amount);

            account.setBalance(account.getBalance().subtract(amount));
            accountDao.update(account, connection);

            final Operation operation = Operation.withdrawal(accountNumber, amount);
            operationDao.save(operation, connection);

            return null;
        });

        LOGGER.info("From account [{}] was withdrawn [{}]", accountNumber, amount);
    }

    /**
     * Transfers money from one account to another
     *
     * @param accountNumberFrom number of account to get money from
     * @param accountNumberTo   number of account to put money to
     * @param amount            amount of money
     */
    public void transfer(Long accountNumberFrom, Long accountNumberTo, BigDecimal amount) {
        Assert.notNull(accountNumberFrom, "accountNumberFrom is null");
        Assert.notNull(accountNumberTo, "accountNumberTo is null");
        Assert.positive(amount, "amount is not positive");
        checkAccountsAreNotSame(accountNumberFrom, accountNumberTo);

        transactionManager.execute(connection -> {
            final Account accountFrom = accountDao.find(accountNumberFrom, connection);
            Assert.found(accountFrom, "Account", accountNumberFrom);

            checkBalanceWithdrawal(accountFrom.getAccountNumber(), accountFrom.getBalance(), amount);

            final Account accountTo = accountDao.find(accountNumberTo, connection);
            Assert.found(accountNumberTo, "Account", accountNumberTo);

            accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
            accountTo.setBalance(accountTo.getBalance().add(amount));

            //Sort accounts according to numbers to avoid of possible deadlocks during update
            final List<Account> accounts = Stream.of(accountFrom, accountTo)
                    .sorted(Comparator.comparing(Account::getAccountNumber))
                    .collect(Collectors.toList());

            for (Account account : accounts) {
                accountDao.update(account, connection);
            }

            final Operation operation = Operation.transfer(accountNumberFrom, accountNumberTo, amount);
            operationDao.save(operation, connection);

            return null;

        });

        LOGGER.info("From account [{}] to account [{}] was transferred [{}]", accountNumberFrom, accountNumberTo, amount);
    }

    /**
     * Fetches operation history for account
     *
     * @param accountNumber number of account to put
     * @return List of {@link Operation}
     */
    public List<Operation> getHistory(Long accountNumber) {
        Assert.notNull(accountNumber, "accountNumber is null");

        return transactionManager.executeReadOnly(connection ->
                operationDao.findAll(accountNumber, connection));
    }

    /**
     * Verify if the amount can be debited
     */
    private void checkBalanceWithdrawal(Long accountNumber, BigDecimal balance, BigDecimal amount) {
        if (balance.subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException(accountNumber);
        }
    }

    /**
     * Verify if the accounts are not same
     */
    private void checkAccountsAreNotSame(Long account1, Long account2) {
        if (account1.equals(account2)) {
            throw new InvalidParametersException("Accounts are the same");
        }
    }

}
