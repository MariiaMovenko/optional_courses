package ua.nure.movenko.summaryTask4.db.transaction;

import ua.nure.movenko.summaryTask4.db.command.SQLCommand;

public interface TransactionManager {
    /**
     * Gets connection to the Database and executes  given {@code command} either in transaction or not depending on
     * {@code transactionLevel} parameter
     *
     * @param command          is command to execute
     * @param transactionLevel defines the TransactionLevel that should be used during executing {@code command}
     * @param <T>              is returned by {@code command} object
     * @return {@code T} object
     */
    <T> T execute(SQLCommand<T> command, int transactionLevel);

}
