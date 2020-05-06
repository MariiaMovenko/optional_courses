package ua.nure.movenko.summaryTask4.db.dao;

import org.apache.log4j.Logger;
import ua.nure.movenko.summaryTask4.entities.Entity;
import ua.nure.movenko.summaryTask4.exception.OperationDaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class based  on implementation of the {@code OperatioDAO<T>} interface.
 *
 * @ author M.Movenko
 */
public abstract class AbstractOperationDAO<T extends Entity> implements OperationDAO<T> {

    private static final Logger LOG = Logger.getLogger(AbstractOperationDAO.class);

    /**
     * Returns all {@code T} objects from Database
     *
     * @param sql        contains Sql command to Database
     * @param connection to Database
     * @return {@code List<T>} of objects obtained by {@code sql} statement
     */
    protected List<T> getAll(String sql, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            return executeQuery(preparedStatement);
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + sql + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + sql + "']", e);
        }
    }

    /**
     * Gets defined by parameters below quantity of {@code T} objects of the particular class from the Database
     *
     * @param sql        is precompiled SQL statement
     * @param connection to Database
     * @param limit      defines how much results should be returned from database
     * @param offset     defines number of results to skip before form the result list
     * @return {@code List<T>} objects
     */
    protected List<T> getAll(String sql, Connection connection, Integer limit, Integer offset) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if (limit != null && offset != null) {
                int k = 1;
                preparedStatement.setInt(k++, limit);
                preparedStatement.setInt(k, offset);
            }
            return executeQuery(preparedStatement);
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + sql + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + sql + "']", e);
        }
    }

    /**
     * @param id         is criteria  for {@code T} object to be found by
     * @param sql        is precompiled SQL statement
     * @param connection to Database
     * @return {@code null} if there were no matches  of the {@code id} criteria  in the Database
     * @throws OperationDaoException when defined {@code sql} command can not be understood or executed by the Database
     */
    protected T getById(int id, String sql, Connection connection) throws OperationDaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            List<T> records = executeQuery(preparedStatement);
            return records.isEmpty() ? null : records.get(0);
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + sql + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + sql + "']", e);
        }
    }

    /**
     * This method returns list of {@code T} objects where each object was formed  on extracted from the Database information
     *
     * @param preparedStatement preparedStatement
     * @return list of {@code T} objects  where each object was formed on the base of extracted from the Database information
     * @throws SQLException when defined  command can not be understood or executed by the Database
     */
    protected List<T> executeQuery(PreparedStatement preparedStatement) throws SQLException {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            List<T> results = new ArrayList<>();
            while (resultSet.next()) {
                T record = extract(resultSet);
                results.add(record);
            }
            return results;
        }
    }

    /**
     * Executes the SQL statement in this <code>PreparedStatement</code> object and removes object that
     * matches {@code id} criteria from the Database
     *
     * @param id         is criteria  for {@code T} object to be found by
     * @param sql        is a {@code String} that contains Sql statement
     * @param connection to Database
     * @return {@code true} if at least one raw in the Database has been  affected after this method was executed
     * @throws OperationDaoException when defined  command can not be understood or executed by the Database
     */
    protected boolean deleteById(long id, String sql, Connection connection) throws OperationDaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + sql + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + sql + "']", e);
        }
    }

    /**
     * Method forms T object from the data represented in {@code resultSet} and returns formed object
     *
     * @param resultSet is table of data representing a database result set, which
     *                  * is usually generated by executing a statement that queries the database.
     * @return {@code T} object formed on the base of {@code resultSet} data
     * @throws SQLException when required information can not be got from {@code resultSet}
     */
    protected abstract T extract(ResultSet resultSet) throws SQLException;

}
