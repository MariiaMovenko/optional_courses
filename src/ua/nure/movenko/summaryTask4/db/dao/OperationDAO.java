package ua.nure.movenko.summaryTask4.db.dao;


import ua.nure.movenko.summaryTask4.entities.Entity;

import java.sql.Connection;
import java.util.List;

public interface OperationDAO<T extends Entity> {
    /**
     * Method inserts in the Database new raw which represents information about {@code entity}
     *
     * @param entity     an object to be added into the Database
     * @param connection to Database
     * @return generated in the Database for added entity int value
     */

    int add(T entity, Connection connection);

    /**
     * Method changes some values in database depending on {@code entity} parameters
     *
     * @param entity     contains parameters that define which entity and how should be changed by this method
     * @param connection to Database
     * @return {@code true} if at least one raw was affected by this method
     */
    boolean update(T entity, Connection connection);

    /**
     * Removes entity from the Database by entity's {@code id}
     *
     * @param id         defines the id of the element in Database to be deleted
     * @param connection to Database
     * @return {@code true} if if at least one value was affected by this method
     */
    boolean deleteById(int id, Connection connection);

    /**
     * Returns an object with the specified {@code id} from Database
     *
     * @param id         is  criteria to find the object by
     * @param connection to Database
     * @return {@code null} if there were no matches of the {@code id} in Database
     */
    T getById(int id, Connection connection);

    /**
     * Gets defined by parameters below quantity of objects of the particular class from the Database
     *
     * @param connection to Database
     * @param limit      defines how much results should be returned from database
     * @param offset     defines number of results to skip before form the result list
     * @return  {@code List<T>} objects
     */
    List<T> getAll(Connection connection, Integer limit, Integer offset);

    /**
     * Gets all occurrences of objects of the particular class from the Database
     *
     * @param connection to Database
     * @return {@code List<T>} objects
     */
    List<T> getAll(Connection connection);
}
