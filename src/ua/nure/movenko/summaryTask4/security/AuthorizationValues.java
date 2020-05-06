package ua.nure.movenko.summaryTask4.security;

/**
 * Holder for authorization conditions and  values corresponding to them.
 *
 * @author M.Movenko
 */
public final class AuthorizationValues {

    private AuthorizationValues() {
    }

    public static final int ALLOWED = 1;

    public static final int FORBIDDEN = 2;

    public static final int UNAUTHORIZED = 3;

    public static final int NOT_FOUND = 4;
}
