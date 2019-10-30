package com.revolut.task.web;

import com.revolut.task.model.Operation;
import com.revolut.task.service.OperationService;
import com.revolut.task.web.dto.OneAccountOperationRequestDto;
import com.revolut.task.web.dto.TwoAccountsOperationRequestDto;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Operations REST endpoint
 * */
@Path("/operations")
public class OperationEndpoint {

    private static final String COMPLETED = "Operation completed";

    private OperationService operationService;

    @Inject
    public OperationEndpoint(OperationService operationService) {
        this.operationService = operationService;
    }

    /**
     * Deposits an account with money
     *
     * @param params request data
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/deposit")
    public Response deposit(@Valid OneAccountOperationRequestDto params) {
        operationService.deposit(params.getAccountNumber(), params.getAmount());
        return Response.ok().entity(COMPLETED).build();
    }

    /**
     * Withdraw money from account
     *
     * @param params request data
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/withdraw")
    public Response withdraw(@Valid OneAccountOperationRequestDto params) {
        operationService.withdraw(params.getAccountNumber(), params.getAmount());
        return Response.ok().entity(COMPLETED).build();
    }

    /**
     * Transfers money from one account to another
     *
     * @param params request data
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/transfer")
    public Response transfer(@Valid TwoAccountsOperationRequestDto params) {
        operationService.transfer(params.getAccountFrom(), params.getAccountTo(), params.getAmount());
        return Response.ok().entity(COMPLETED).build();
    }

    /**
     * Retrieves full history of operations with account
     *
     * @param accountNumber
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/history")
    public List<Operation> getHistory(@QueryParam("accountNumber") Long accountNumber) {
        return operationService.getHistory(accountNumber);
    }
}
