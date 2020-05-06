package ua.nure.movenko.summaryTask4.exception;

/**
 * An exception that provides information on an application error.
 *
 * @author M.Movenko
 *
 */
public class ApplicationException extends RuntimeException {

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

}
