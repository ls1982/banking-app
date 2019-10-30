package com.revolut.task.config;

import com.revolut.task.repository.AccountDao;
import com.revolut.task.repository.DefaultTransactionManager;
import com.revolut.task.repository.OperationDao;
import com.revolut.task.repository.TransactionManager;
import com.revolut.task.service.AccountService;
import com.revolut.task.service.OperationService;
import com.revolut.task.web.AccountEndpoint;
import com.revolut.task.web.OperationEndpoint;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import javax.ws.rs.ApplicationPath;

/**
 * Application resource config.
 * Registers endpoints and bind dependencies
 *
 * @author Alexey Smirnov
 */
@ApplicationPath("/")
public class ApplicationConfig extends ResourceConfig {
	public ApplicationConfig() {
		register(AccountEndpoint.class);
		register(OperationEndpoint.class);
		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bindAsContract(AccountService.class);
				bindAsContract(OperationService.class);
				bindAsContract(AccountDao.class);
				bindAsContract(OperationDao.class);
				bind(DefaultTransactionManager.class).to(TransactionManager.class);

			}
		});
		property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
		register(JacksonFeature.class);
	}
}
