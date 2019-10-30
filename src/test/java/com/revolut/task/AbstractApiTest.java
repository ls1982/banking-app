package com.revolut.task;


import com.revolut.task.model.Account;
import com.revolut.task.web.dto.OneAccountOperationRequestDto;
import com.revolut.task.web.dto.TwoAccountsOperationRequestDto;
import org.glassfish.jersey.test.JerseyTest;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public abstract class AbstractApiTest extends JerseyTest {
	protected Account createAccount() {
		final Response response = target("accounts").request().post(null);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

		return response.readEntity(Account.class);
	}

	protected Account getAccount(long accountNumber) {
		final Response response = target("accounts/" + accountNumber).request().get();
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

		return response.readEntity(Account.class);
	}

	protected Response deposit(long accountNumber, BigDecimal amount) {
		final OneAccountOperationRequestDto depositRequest = new OneAccountOperationRequestDto();
		depositRequest.setAccountNumber(accountNumber);
		depositRequest.setAmount(amount);

		return target("operations/deposit").request(MediaType.APPLICATION_JSON_TYPE)
				.put(Entity.entity(depositRequest, MediaType.APPLICATION_JSON_TYPE));
	}

	protected Response withdraw(long accountNumber, BigDecimal amount) {
		final OneAccountOperationRequestDto withdrawRequest = new OneAccountOperationRequestDto();
		withdrawRequest.setAccountNumber(accountNumber);
		withdrawRequest.setAmount(amount);

		return target("operations/withdraw").request(MediaType.APPLICATION_JSON_TYPE)
				.put(Entity.entity(withdrawRequest, MediaType.APPLICATION_JSON_TYPE));
	}

	/** Transfers amount of units from accountFrom to accountTo */
	protected Response transfer(long accountFrom, long accountTo, BigDecimal amount) {

		final TwoAccountsOperationRequestDto transferRequest = new TwoAccountsOperationRequestDto();
		transferRequest.setAccountFrom(accountFrom);
		transferRequest.setAccountTo(accountTo);
		transferRequest.setAmount(amount);

		return target("operations/transfer").request(MediaType.APPLICATION_JSON_TYPE)
				.put(Entity.entity(transferRequest, MediaType.APPLICATION_JSON_TYPE));
	}

	protected void checkResponseIsOk(Response response){
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	};
}
