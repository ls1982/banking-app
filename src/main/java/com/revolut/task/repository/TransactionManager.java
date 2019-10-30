package com.revolut.task.repository;

/**
 * Provides transactional wrappers
 *
 * @author Alexey Smirnov
 */
public interface TransactionManager {
	/**
	 * Performs amount of work inside a transaction
	 *
	 * @param work work that should be done
	 * @return result of work
	 */
	<R> R execute(TransactionalWork<R> work);

	/**
	 * Performs amount of work inside a read-only transaction
	 *
	 * @param work work to be done
	 * @return result of work
	 */
	<R> R executeReadOnly(TransactionalWork<R> work);
}
