package com.revolut.task;


import com.revolut.task.model.Account;
import com.revolut.task.web.dto.AmountRequestDto;
import com.revolut.task.web.dto.TransferRequestDto;
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
		final Response response = target(String.format("accounts/%s", accountNumber)).request().get();
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

		return response.readEntity(Account.class);
	}

	protected Response deposit(long accountNumber, BigDecimal amount) {
		final AmountRequestDto depositRequest = new AmountRequestDto();
		depositRequest.setAmount(amount);

		return target(String.format("accounts/%s/deposit", accountNumber)).request(MediaType.APPLICATION_JSON_TYPE)
				.put(Entity.entity(depositRequest, MediaType.APPLICATION_JSON_TYPE));
	}

	protected Response withdraw(long accountNumber, BigDecimal amount) {
		final AmountRequestDto withdrawRequest = new AmountRequestDto();
		withdrawRequest.setAmount(amount);

		return target(String.format("accounts/%s/withdraw", accountNumber)).request(MediaType.APPLICATION_JSON_TYPE)
				.put(Entity.entity(withdrawRequest, MediaType.APPLICATION_JSON_TYPE));
	}

	/** Transfers amount of units from accountFrom to accountTo */
	protected Response transfer(long accountFrom, long accountTo, BigDecimal amount) {

		final TransferRequestDto transferRequest = new TransferRequestDto();
		transferRequest.setAccountTo(accountTo);
		transferRequest.setAmount(amount);

		return target(String.format("accounts/%s/transfer", accountFrom)).request(MediaType.APPLICATION_JSON_TYPE)
				.put(Entity.entity(transferRequest, MediaType.APPLICATION_JSON_TYPE));
	}

	protected void checkResponseIsOk(Response response){
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	};
}
