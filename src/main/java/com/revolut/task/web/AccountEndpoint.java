package com.revolut.task.web;

import com.revolut.task.model.Account;
import com.revolut.task.service.AccountService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Account REST endpoint
 *
 * @author Alexey Smirnov
 * */
@Path("/accounts")
public class AccountEndpoint {

    private AccountService accountService;

    @Inject
    public AccountEndpoint(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Creates a new account
     * @return {@link Account} created account
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Account createAccount() {
        return accountService.createAccount();
    }

    /**
     * Finds an account by the account number
     *
     * @param accountNumber the account number to search
     * @return {@link Account} account found
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{accountNumber}")
    public Account findAccount(@PathParam("accountNumber") Long accountNumber) {
        return accountService.findAccount(accountNumber);
    }
}
