package ua.nure.movenko.summaryTask4.enums;

/**
 * Role entity.
 *
 * @author M.Movenko
 */
public enum Role {

    ADMIN, LECTOR, STUDENT, GUEST;

    public static Role getRole(int ordinal) {
        return Role.values()[ordinal];
    }

    public String getName() {
        return name().toLowerCase();
    }
}
