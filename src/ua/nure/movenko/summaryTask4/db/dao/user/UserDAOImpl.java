package ua.nure.movenko.summaryTask4.db.dao.user;

import org.apache.log4j.Logger;
import ua.nure.movenko.summaryTask4.db.dao.AbstractOperationDAO;
import ua.nure.movenko.summaryTask4.constants.DBFields;
import ua.nure.movenko.summaryTask4.enums.Role;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.UserStatus;
import ua.nure.movenko.summaryTask4.exception.OperationDaoException;

import java.sql.*;
import java.util.List;

/**
 * * UserDAOImpl based  on implementation of the {@code UserDAO} interface.
 * *
 * * @ author M.Movenko
 */
public class UserDAOImpl extends AbstractOperationDAO<User> implements UserDAO {

    private static final Logger LOG = Logger.getLogger(UserDAOImpl.class);

    private static final String GET_USER_BY_ID = "SELECT * FROM users WHERE id=?";
    private static final String GET_USER_BY_NAME_SURNAME = "SELECT * FROM users WHERE first_name = ? AND last_name = ?";
    private static final String GET_USER_BY_EMAIL = "SELECT * FROM users WHERE email=?";
    private static final String GET_ALL_USERS = "SELECT * FROM users";
    private static final String GET_USER_BY_LOGIN = "SELECT * FROM users WHERE login=? COLLATE utf8mb4_0900_as_cs";
    private static final String DELETE_BY_ID = "DELETE FROM users WHERE id =?";
    private static final String DELETE_PENDING_USERS = "DELETE FROM users  WHERE  status_id=?";
    private static final String ADD_USER = "INSERT INTO users VALUES ( DEFAULT, ?, ?, ?, ?, ?,?, DEFAULT, DEFAULT)";
    private static final String UPDATE_USER = "UPDATE users SET  login=?, email=?, first_name=?, last_name=? WHERE id=?";
    private static final String CHANGE_AVATAR = "UPDATE users SET  avatar=? WHERE id=?";
    private static final String GET_USERS_BY_ROLE = "SELECT * FROM users WHERE role_id =?";
    private static final String GET_STUDENTS_BY_STATUS = "SELECT * FROM users WHERE role_id = 2 AND status_id=?";
    private static final String UPDATE_PASSWORD = "UPDATE users SET  password=? WHERE id=?";
    private static final String UPDATE_STUDENT_STATUS_BY_LOGIN = "UPDATE users SET status_id=? WHERE login=?";
    private static final String CONFIRM_REGISTRATION = "UPDATE users SET status_id=? WHERE id=? AND login=? AND status_id=?";

    @Override
    public User getByLogin(String login, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_LOGIN)) {
            preparedStatement.setString(1, login);
            List<User> records = executeQuery(preparedStatement);
            return records.isEmpty() ? null : records.get(0);
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + GET_USER_BY_LOGIN + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + GET_USER_BY_LOGIN + "']", e);
        }
    }

    @Override
    public User getByEmail(String email, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_EMAIL)) {
            preparedStatement.setString(1, email);
            List<User> records = executeQuery(preparedStatement);
            return records.isEmpty() ? null : records.get(0);
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + GET_USER_BY_EMAIL + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + GET_USER_BY_EMAIL + "']", e);
        }
    }

    @Override
    public User getByNameAndSurname(String firstName, String surname, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_NAME_SURNAME)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, surname);
            List<User> records = executeQuery(preparedStatement);
            return records.isEmpty() ? null : records.get(0);
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + GET_USER_BY_NAME_SURNAME + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + GET_USER_BY_NAME_SURNAME + "']", e);
        }
    }

    @Override
    public List<User> getUsersByRole(Role role, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_USERS_BY_ROLE)) {
            int k = 1;
            preparedStatement.setInt(k, role.ordinal());
            return executeQuery(preparedStatement);
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + GET_USERS_BY_ROLE + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + GET_USERS_BY_ROLE + "']", e);
        }
    }


    @Override
    public List<User> getStudentsByStatus(UserStatus status, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_STUDENTS_BY_STATUS)) {
            preparedStatement.setInt(1, status.ordinal());
            return executeQuery(preparedStatement);
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + GET_STUDENTS_BY_STATUS + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + GET_STUDENTS_BY_STATUS + "']", e);
        }
    }

    @Override
    public boolean exists(String login, Connection connection) {

        return getByLogin(login, connection) != null;
    }

    @Override
    public boolean existsByEmail(String email, Connection connection) {
        return getByEmail(email, connection) != null;
    }

    @Override
    public int add(User user, Connection connection) {
        if (!exists(user.getLogin(), connection)) {
            try (PreparedStatement preparedStatement = connection
                    .prepareStatement(ADD_USER, Statement.RETURN_GENERATED_KEYS)) {
                int k = 1;
                preparedStatement.setString(k++, user.getLogin());
                preparedStatement.setString(k++, user.getPassword());
                preparedStatement.setString(k++, user.getEmail());
                preparedStatement.setString(k++, user.getFirstName());
                preparedStatement.setString(k++, user.getLastName());
                preparedStatement.setInt(k, user.getRole().ordinal());
                if (preparedStatement.executeUpdate() > 0) {
                    ResultSet resultSet = preparedStatement.getGeneratedKeys();
                    if (resultSet.next()) {
                        user.setId(resultSet.getInt(1));
                    }
                }
            } catch (SQLException e) {
                LOG.error("Can't handle sql ['" + ADD_USER + "']", e);
                throw new OperationDaoException("Can't handle sql ['" + ADD_USER + "']", e);
            }
        }
        return user.getId();
    }

    @Override
    public boolean update(User user, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER)) {
            int k = 1;
            preparedStatement.setString(k++, user.getLogin());
            preparedStatement.setString(k++, user.getEmail());
            preparedStatement.setString(k++, user.getFirstName());
            preparedStatement.setString(k++, user.getLastName());
            preparedStatement.setLong(k, user.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + UPDATE_USER + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + UPDATE_USER + "'] ", e);
        }
    }

    @Override
    public boolean changeAvatar(User user, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_AVATAR)) {
            int k = 1;
            preparedStatement.setString(k++, user.getAvatarPath());
            preparedStatement.setLong(k, user.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + CHANGE_AVATAR + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + CHANGE_AVATAR + "'] ", e);
        }
    }

    @Override
    public boolean updateStatus(UserStatus status, Connection connection, String... logins) {
        int rawsAffected = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STUDENT_STATUS_BY_LOGIN)) {
            int k = 1;
            preparedStatement.setInt(k++, status.ordinal());
            for (String login : logins) {
                preparedStatement.setString(k, login);
                rawsAffected += preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + UPDATE_STUDENT_STATUS_BY_LOGIN + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + UPDATE_STUDENT_STATUS_BY_LOGIN + "'] ", e);
        }
        return rawsAffected >= 1;
    }

    @Override
    public boolean changePassword(User user, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PASSWORD)) {
            int k = 1;
            preparedStatement.setString(k++, user.getPassword());
            preparedStatement.setLong(k, user.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + UPDATE_PASSWORD + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + UPDATE_PASSWORD + "'] ", e);
        }
    }

    @Override
    public boolean confirmRegistration(Connection connection, String login, int id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(CONFIRM_REGISTRATION)) {
            int k = 1;
            preparedStatement.setInt(k++, UserStatus.ACTIVE.ordinal());
            preparedStatement.setInt(k++, id);
            preparedStatement.setString(k++, login);
            preparedStatement.setInt(k, UserStatus.PENDING.ordinal());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + CONFIRM_REGISTRATION + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + CONFIRM_REGISTRATION + "'] ", e);
        }
    }

    @Override
    public boolean deleteById(int id, Connection connection) {
        return deleteById(id, DELETE_BY_ID, connection);
    }


    @Override
    public boolean deletePendingUsers(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PENDING_USERS)) {
            preparedStatement.setInt(1, UserStatus.PENDING.ordinal());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + DELETE_PENDING_USERS + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + DELETE_PENDING_USERS + "']", e);
        }
    }

    @Override
    public User getById(int id, Connection connection) {
        return getById(id, GET_USER_BY_ID, connection);
    }

    @Override
    public List<User> getAll(Connection connection, Integer limit, Integer offset) {
        return getAll(GET_ALL_USERS, connection, limit, offset);
    }

    @Override
    public List<User> getAll(Connection connection) {
        return getAll(GET_ALL_USERS, connection);
    }

    @Override
    protected User extract(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt(DBFields.ENTITY_ID));
        user.setLogin(resultSet.getString(DBFields.USER_LOGIN));
        user.setPassword(resultSet.getString(DBFields.USER_PASSWORD));
        user.setEmail(resultSet.getString(DBFields.USER_EMAIL));
        user.setRole(Role.getRole(resultSet.getInt(DBFields.USER_ROLE_ID)));
        user.setFirstName(resultSet.getString(DBFields.USER_FIRST_NAME));
        user.setLastName(resultSet.getString(DBFields.USER_LAST_NAME));
        user.setStatus(UserStatus.getStatus(resultSet.getInt(DBFields.USER_STATUS_ID)));
        user.setAvatarPath(resultSet.getString(DBFields.PATH_TO_AVATAR));
        return user;

    }
}
