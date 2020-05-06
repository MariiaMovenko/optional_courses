package ua.nure.movenko.summaryTask4.db.command;

import java.sql.Connection;
import java.sql.SQLException;

public interface SQLCommand<T> {
    /**
     * Executes commands to Database
     *
     * @param connection to Database
     * @return {@code T} object which has been returned by executed command
     */
    T execute(Connection connection);

}
