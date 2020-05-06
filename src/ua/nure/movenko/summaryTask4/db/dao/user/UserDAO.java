package ua.nure.movenko.summaryTask4.db.dao.user;

import ua.nure.movenko.summaryTask4.db.dao.OperationDAO;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.Role;
import ua.nure.movenko.summaryTask4.enums.UserStatus;

import java.sql.Connection;
import java.util.List;

public interface UserDAO extends OperationDAO<User> {

    /**
     * Returns user with the specified login
     *
     * @param login      is criteria to find the user by
     * @param connection to database
     * @return {@code null} if there no users with the specified {@code login} in the database
     */
    User getByLogin(String login, Connection connection);

    /**
     * Returns user with the specified {@code email}
     *
     * @param email      is criteria to find the user by
     * @param connection to database
     * @return {@code null} if there no users with the specified {@code email} in the database
     */
    User getByEmail(String email, Connection connection);

    /**
     * Returns user with the specified {@code firstName} and {@code surname}
     *
     * @param firstName  together with {@code surname} is criteria to find the user
     * @param surname    together with {@code firstName} is criteria to find the user
     * @param connection to database
     * @return {@code null} if there no users with the specified firstName and surname in the database
     */
    User getByNameAndSurname(String firstName, String surname, Connection connection);

    /**
     * Checks if an object with the specified parameters exists in the Database
     *
     * @param login      is criteria to find the user by
     * @param connection to Database
     * @return {@code false} if there no users with the specified parameters in the database
     */
    boolean exists(String login, Connection connection);

    /**
     * Checks if an object with the specified {@code email} exists in the Database
     *
     * @param email      is criteria to find the user by
     * @param connection to database
     * @return {@code false} if there no users with the specified parameter in the database
     */
    boolean existsByEmail(String email, Connection connection);

    /**
     * Returns all Users chosen by {@code role}
     * @param role is criteria to choose users
     * @param connection to Database
     * @return the list of {@code User} users
     */
    List<User> getUsersByRole (Role role, Connection connection);

    /**
     * Returns  from database all users whose role is (@equals(Role.STUDENT))
     *
     * @param connection to Database
     * @return the list of {@code User} users whose role in the database {@code equals(Role.STUDENT)}
     */

    List<User> getStudentsByStatus(UserStatus status, Connection connection);

    /**
     * Replaces actual status of user in Database with {@code status}
     *
     * @param status     defines actual status of user with {@code login} to be changed to
     * @param connection to Database
     * @param login      defines user(s) whose status should be changed to {@code status}
     * @ return {@code true} if no exceptions were thrown during the executing of this method
     */
    boolean updateStatus(UserStatus status, Connection connection, String... login);

    /**
     * Replaces actual path to user's avatar in Database with new one or adds if actual path is absent
     * @param user information on whose avatar should be changed
     * @param connection to Database
     * @return {@code true} if at least one raw at Database was effected by this method
     */
    boolean changeAvatar(User user, Connection connection);
    /**
     * Changes actual {@code UserStatus.PENDING} of particular user to {@code UserStatus.ACTIVE}
     *
     * @param connection to Database
     * @param login      together with {@code id} defines user whose status should be changed to {@code UserStatus.ACTIVE}
     * @param id         together with {@code login} defines user whose status should be changed to {@code UserStatus.ACTIVE}
     * @return {@code true} if at least one raw at Database was effected by this method
     */
    boolean confirmRegistration(Connection connection, String login, int id);

    /**
     * Removes from the database all users with {@code UserStatus.Pending}
     *
     * @param connection to Database
     * @return {@code true} if at least one raw at Database was effected by this method
     */
    boolean deletePendingUsers(Connection connection);

    /**
     * Method replaces  old (defined in Database) password of user with {@code user.getId()}
     * to new password {@code user.getPassword()}
     *
     * @param user       containes up-to-date information about
     * @param connection to Database
     * @return {@code true} if a raw at Database was effected by this method
     */
    boolean changePassword(User user, Connection connection);

}
