package ua.nure.movenko.summaryTask4.models;

/**
 * LectorModel entity. Adapts User entity with {@code Role.LECTOR} role to view layer.
 *
 * @author M.Movenko
 */
public class LectorModel {
    private int lectorId;
    private String firstName;
    private String lastName;
    private String email;

    public int getLectorId() {
        return lectorId;
    }

    public void setLectorId(int lectorId) {
        this.lectorId = lectorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
