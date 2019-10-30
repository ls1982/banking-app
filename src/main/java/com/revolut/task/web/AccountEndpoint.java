package com.revolut.task.web;

import com.revolut.task.model.Account;
import com.revolut.task.model.Operation;
import com.revolut.task.service.AccountService;
import com.revolut.task.service.OperationService;
import com.revolut.task.web.dto.AmountRequestDto;
import com.revolut.task.web.dto.TransferRequestDto;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Account REST endpoint
 *
 * @author Alexey Smirnov
 * */
@Path("/accounts")
public class AccountEndpoint {

    private static final String COMPLETED = "Operation completed";

    private AccountService accountService;
    private OperationService operationService;

    @Inject
    public AccountEndpoint(AccountService accountService, OperationService operationService) {
        this.accountService = accountService;
        this.operationService = operationService;
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

    /**
     * Deposits an account with money
     *
     * @param params request data
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{accountNumber}/deposit")
    public Response deposit(@PathParam("accountNumber") @NotNull(message = "Account number can't be empty") Long accountNumber,
                            @Valid AmountRequestDto params) {
        operationService.deposit(accountNumber, params.getAmount());
        return Response.ok().entity(COMPLETED).build();
    }

    /**
     * Withdraw money from account
     *
     * @param params request data
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{accountNumber}/withdraw")
    public Response withdraw(@PathParam("accountNumber") @NotNull(message = "Account number can't be empty") Long accountNumber,
                             @Valid AmountRequestDto params) {
        operationService.withdraw(accountNumber, params.getAmount());
        return Response.ok().entity(COMPLETED).build();
    }

    /**
     * Transfers money from one account to another
     *
     * @param params request data
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{accountNumber}/transfer")
    public Response transfer(@PathParam("accountNumber") @NotNull(message = "Account number can't be empty") Long accountNumber,
                             @Valid TransferRequestDto params) {
        operationService.transfer(accountNumber, params.getAccountTo(), params.getAmount());
        return Response.ok().entity(COMPLETED).build();
    }

    /**
     * Retrieves full history of operations with account
     *
     * @param accountNumber
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{accountNumber}/history")
    public List<Operation> getHistory(@PathParam("accountNumber") Long accountNumber) {
        return operationService.getHistory(accountNumber);
    }
}
