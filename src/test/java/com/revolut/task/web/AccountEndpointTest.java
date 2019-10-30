package com.revolut.task.web;

import com.revolut.task.AbstractApiTest;
import com.revolut.task.config.ApplicationConfig;
import com.revolut.task.model.Account;
import org.junit.Test;

import javax.ws.rs.core.Application;

import static org.junit.Assert.assertEquals;

public class AccountEndpointTest extends AbstractApiTest {

    @Test
    public void createAndGetAccountSuccessfully() {
        final Account account = createAccount();
        getAccount(account.getAccountNumber());
    }

    @Override
    protected Application configure() {
        return new ApplicationConfig();
    }
}
