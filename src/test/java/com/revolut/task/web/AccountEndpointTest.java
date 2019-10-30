package com.revolut.task.web;

import com.revolut.task.AbstractApiTest;
import com.revolut.task.config.ApplicationConfig;
import com.revolut.task.model.Account;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class AccountEndpointTest extends AbstractApiTest {

    @Test
    public void createAndGetAccountSuccessfully() {
        final Account account = createAccount();
        getAccount(account.getAccountNumber());
    }

    @Test
    public void depositAccountSuccessfully() {
        Account account = createAccount();

        final Response response = deposit(account.getAccountNumber(), BigDecimal.TEN);

        checkResponseIsOk(response);

        account = getAccount(account.getAccountNumber());

        assertEquals(0, BigDecimal.TEN.compareTo(account.getBalance()));
    }

    @Test
    public void depositAccountWithNullAmountFailed() {
        final Response response = deposit(0, null);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void withdrawMoneySuccessfully() {
        Account account = createAccount();

        Response response  = deposit(account.getAccountNumber(), BigDecimal.valueOf(20));

        checkResponseIsOk(response);

        account = getAccount(account.getAccountNumber());
        assertEquals(0, BigDecimal.valueOf(20).compareTo(account.getBalance()));

        response = withdraw(account.getAccountNumber(), BigDecimal.TEN);

        checkResponseIsOk(response);

        account = getAccount(account.getAccountNumber());
        assertEquals(0, BigDecimal.TEN.compareTo(account.getBalance()));
    }

    @Test
    public void withdrawNullAmountFailed() {
        final Response response = withdraw(0, null);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void transferMoneySuccessfully() {
        // create accountFrom
        Account accountFrom = createAccount();

        // deposit accountFrom with 20 units
        Response response = deposit(accountFrom.getAccountNumber(), BigDecimal.valueOf(20));

        checkResponseIsOk(response);

        // create accountTo
        Account accountTo = createAccount();

        // transfer 10 units from accountFrom to accountTo
        response = transfer(accountFrom.getAccountNumber(), accountTo.getAccountNumber(), BigDecimal.TEN);

        checkResponseIsOk(response);

        // check the result balance
        accountFrom = getAccount(accountFrom.getAccountNumber());
        assertEquals(0, BigDecimal.TEN.compareTo(accountFrom.getBalance()));

        accountTo = getAccount(accountTo.getAccountNumber());
        assertEquals(0, BigDecimal.TEN.compareTo(accountTo.getBalance()));
    }

    @Test
    public void transferNullAmountFailed() {
        final Response response = transfer(0, 0, null);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Override
    protected Application configure() {
        return new ApplicationConfig();
    }
}
