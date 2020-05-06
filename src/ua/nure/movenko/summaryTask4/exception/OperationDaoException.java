package ua.nure.movenko.summaryTask4.exception;

/**
 * An exception that provides information on database access  error .
 *
 * @author M. Movenko
 */
public class OperationDaoException extends RuntimeException {

    public OperationDaoException(String message) {
        super(message);
    }

    public OperationDaoException(String message, Throwable cause) {
        super(message, cause);
    }

}
