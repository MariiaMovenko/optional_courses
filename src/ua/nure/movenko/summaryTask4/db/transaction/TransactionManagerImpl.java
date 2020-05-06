package ua.nure.movenko.summaryTask4.db.transaction;

import org.apache.log4j.Logger;
import ua.nure.movenko.summaryTask4.db.command.SQLCommand;
import ua.nure.movenko.summaryTask4.exception.ApplicationException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * TransactionManagerImpl based  on implementation of the {@code TransactionManager} interface.
 *
 *  @ author M.Movenko
 */
public class TransactionManagerImpl implements TransactionManager {

    private static final Logger LOG = Logger.getLogger(TransactionManagerImpl.class);

    private final DataSource dataSource;

    public TransactionManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public <T> T execute(SQLCommand<T> command, int transactionLevel) {
        T result;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            result = executeInTransaction(command, connection, transactionLevel);
        } catch (Exception e) {
            LOG.error( e);
            if (connection != null) {
                rollback(connection);
            }
            throw  new ApplicationException("Can'n execute command " + command, e);
        } finally {
            if (connection != null) {
                close(connection);
            }
        }
        return result;
    }

    <T> T executeInTransaction(SQLCommand<T> command, Connection connection, int transactionLevel) throws Exception {
        if (transactionLevel == Connection.TRANSACTION_NONE) {
            return command.execute(connection);
        }
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(transactionLevel);
        T result = command.execute(connection);
        connection.commit();
        return result;
    }

    private void rollback(Connection con) {
        try {
            con.rollback();
        } catch (SQLException e) {
            LOG.error(e);
        }
    }

    private void close(Connection con) {
        try {
            con.close();
        } catch (SQLException e) {
            LOG.error(e);
        }
    }
}
