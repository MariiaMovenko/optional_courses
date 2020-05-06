package ua.nure.movenko.summaryTask4.db.transaction;

import ua.nure.movenko.summaryTask4.db.DataSourceMock;
import ua.nure.movenko.summaryTask4.db.command.SQLCommand;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class TransactionManagerMock implements TransactionManager {

    private SQLCommand lastExecutedCommand;

    private final DataSource dataSource = new DataSourceMock();

    private final TransactionManagerImpl transactionManager = spy(new TransactionManagerImpl(dataSource));

    @Override
    public <T> T execute(SQLCommand<T> command, int transactionLevel) {
        lastExecutedCommand = command;
        return transactionManager.execute(command, transactionLevel);
    }

    public void verifyExecutedInTransaction(int transactionLevel) throws Exception {
        verify(transactionManager).executeInTransaction(lastExecutedCommand, dataSource.getConnection(), transactionLevel);
    }

    public void verifyExecutedWithoutTransaction() throws Exception {
        verify(transactionManager).executeInTransaction(lastExecutedCommand, dataSource.getConnection(), Connection.TRANSACTION_NONE);
    }

    public Connection getConnectionForTest() throws SQLException {
        return dataSource.getConnection();
    }

}
