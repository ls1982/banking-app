package com.revolut.task;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;
import com.revolut.task.config.ApplicationConfig;
import com.revolut.task.model.Account;
import com.revolut.task.web.dto.TransferRequestDto;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tests reliability of money transferring in multithreading environment.
 *
 * Initially we create <tt>ACCOUNT_COUNT</tt> accounts and deposit each with
 * <tt>INITIAL_SUM</tt> units.
 *
 * Then money are being transferred randomly with
 * the help of <tt>THREAD_COUNT</tt> threads performing <tt>TRANSFER_LOOPS</tt>
 * transfers.
 *
 * Finally, aggregated balance of all accounts should be equal to initial sum of
 * all accounts before test.
 *
 * @author Alexey Smirnov
 */
@RunWith(ConcurrentTestRunner.class)
public class TransferConcurrentTest extends AbstractApiTest {

	/** Count of accounts for test */
	private final static int ACCOUNT_COUNT = 3;
	/** Number of loops each thread runs */
	private final static int TRANSFER_LOOPS = 100;
	/** Count of threads running the test case */
	private final static int THREAD_COUNT = 40;
	/** Initial balance of each account */
	private final static BigDecimal INITIAL_SUM = BigDecimal.valueOf(10_000);
	private static final Logger LOGGER = LoggerFactory.getLogger(TransferConcurrentTest.class);

	/** List of account numbers created */
	private static List<Long> accountNums = new ArrayList<>();

	@Before
	public void setup() {
		for (int i = 0; i < ACCOUNT_COUNT; i++) {
			Account account = createAccount();
			accountNums.add(account.getAccountNumber());

			deposit(account.getAccountNumber(), INITIAL_SUM);

			LOGGER.info("Account [{}] was filled with [{}] units", account.getAccountNumber(), INITIAL_SUM.doubleValue());
		}
	}

	@Test
	@ThreadCount(THREAD_COUNT)
	public void shouldTransferMoneyAmongAccounts(){
		Assert.assertFalse(accountNums.isEmpty());

		for (int i = 0; i <= TRANSFER_LOOPS; i++) {
			final Long accountFrom = getRandomAccountNum(null);
			final Long accountTo = getRandomAccountNum(accountFrom);
			final BigDecimal amount = getRandomAmount().divide(BigDecimal.valueOf(2), BigDecimal.ROUND_HALF_UP);

			transferIgnoreResult(accountFrom, accountTo, amount);
		}

	}

	@After
	public void after() {
		final BigDecimal balanceSum = accountNums.stream()
				.map(this::getAccount)
				.map(Account::getBalance).reduce(BigDecimal.ZERO, BigDecimal::add);

		final BigDecimal initialBalance = INITIAL_SUM.multiply(BigDecimal.valueOf(accountNums.size()));

		Assert.assertEquals(0, balanceSum.compareTo(initialBalance));
	}

	private void transferIgnoreResult(long accountFrom, long accountTo, BigDecimal amount) {
		final TransferRequestDto transferRequest = new TransferRequestDto();
		transferRequest.setAccountTo(accountTo);
		transferRequest.setAmount(amount);

		final Response response = target(String.format("accounts/%s/transfer", accountFrom)).request(MediaType.APPLICATION_JSON_TYPE)
				.put(Entity.entity(transferRequest, MediaType.APPLICATION_JSON_TYPE));

		if (Response.Status.OK.getStatusCode() == response.getStatus()) {
			LOGGER.info("[{}] units successfully transferred from [{}] to [{}]",
					amount, accountFrom, accountTo);
		} else {
			LOGGER.info("Failed to transfer [{}] units from [{}] to [{}]. Message: [{}]",
					amount, accountFrom, accountTo, response.readEntity(String.class));
		}
	}

	private Long getRandomAccountNum(Long notThat) {
		final List<Long> freeAccounts = accountNums.stream()
				.filter(num -> !num.equals(notThat)).collect(Collectors.toList());
		final int index = Double.valueOf((freeAccounts.size() - 0.1) * Math.random()).intValue();

		return freeAccounts.get(index);
	}

	private BigDecimal getRandomAmount() {
		return INITIAL_SUM.multiply(BigDecimal.valueOf(Math.random()));
	}

	@Override
	protected Application configure() {
		return new ApplicationConfig();
	}

}
