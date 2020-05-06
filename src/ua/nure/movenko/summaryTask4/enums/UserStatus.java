package ua.nure.movenko.summaryTask4.enums;

/**
 * UserStatus entity.
 *
 * @author M.Movenko
 */
public enum UserStatus {
    ACTIVE, BANNED, PENDING;

    public static UserStatus getStatus(int ordinal) {
        return UserStatus.values()[ordinal];
    }
}
