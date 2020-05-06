package ua.nure.movenko.summaryTask4.exception;

/**
 * An exception that provides information on an  sending mail to user error .
 *
 * @author M. Movenko
 */
public class SendingMailException extends RuntimeException {

    public SendingMailException(Throwable cause) {
        super(cause);
    }

    public SendingMailException(String message, Throwable cause) {
        super(message, cause);
    }
}
