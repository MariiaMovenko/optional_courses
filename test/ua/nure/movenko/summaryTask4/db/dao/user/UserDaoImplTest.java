package ua.nure.movenko.summaryTask4.db.dao.user;

import com.mysql.jdbc.Driver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.Role;
import ua.nure.movenko.summaryTask4.enums.UserStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class UserDaoImplTest {

    private final UserDAO userDAO = new UserDAOImpl();
    private Connection connection;


    public static Connection getConnection() {
        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "admin");

        Connection connection = null;
        try {
            connection = ((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance())
                    .connect("jdbc:mysql://localhost:3306/test_db?serverTimezone=UTC", props);
        } catch (ClassNotFoundException e) {
            System.err.println("Where is your JDBC Driver?\n");
            e.printStackTrace();
            System.exit(1);
        } catch (SQLException e) {
            System.err.println("Connection Failed.\n");
            e.printStackTrace();
            System.exit(1);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        return connection;
    }

    @Before
    public void setUp() {
        connection = getConnection();
    }

    @After
    public void tearDown() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE  FROM users");
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            System.err.println("Can't close connection.\n");
            e.printStackTrace();
        }
    }

    @Test
    public void testAddUser() {
        User user = prepareUser(Role.STUDENT, null);

        int id = userDAO.add(user, connection);

        Assert.assertTrue(id > 0);
        Assert.assertNotNull(id);
        System.out.println(user.getId());
    }

    @Test
    public void testConfirmRegistration() {
        User user = prepareUser(Role.STUDENT, null);
        userDAO.add(user, connection);

        boolean confirmed = userDAO.confirmRegistration(connection, user.getLogin(), user.getId());
        Assert.assertTrue(confirmed);

        user.setStatus(UserStatus.BANNED);
        userDAO.update(user, connection);

        boolean confirmedIfNotPending = userDAO.confirmRegistration(connection,user.getLogin(), user.getId());
        Assert.assertFalse(confirmedIfNotPending);
    }

    @Test
    public void testGetByLogin() {
        User user = prepareUser(Role.STUDENT, null);
        Integer userId = userDAO.add(user, connection);

        User result = userDAO.getByLogin(user.getLogin(), connection);

        Assert.assertNotNull(result);
        Assert.assertEquals(userId, result.getId());
        Assert.assertEquals("login", result.getLogin());
        Assert.assertEquals("email@i.ua", result.getEmail());

        User shouldBeNull = userDAO.getByLogin("notRealLogin", connection);

        Assert.assertNull(shouldBeNull);
    }

    @Test
    public void testGetById() {
        User user = prepareUser(Role.STUDENT, null);
        Integer userId = userDAO.add(user, connection);

        User result = userDAO.getById(userId, connection);

        Assert.assertNotNull(result);
        Assert.assertEquals(userId, result.getId());
        Assert.assertEquals("login", result.getLogin());
        Assert.assertEquals("email@i.ua", result.getEmail());
    }

    @Test
    public void testGetByEmail() {
        User user = prepareUser(Role.STUDENT, null);
        Integer userId = userDAO.add(user, connection);

        User result = userDAO.getByEmail("email@i.ua", connection);

        Assert.assertNotNull(result);
        Assert.assertEquals(userId, result.getId());
        Assert.assertEquals("login", result.getLogin());
        Assert.assertEquals("email@i.ua", result.getEmail());

    }

    @Test
    public void testGetByRole() {
        User user1 = prepareUser(Role.STUDENT, null);
        User user2 = prepareUser(Role.ADMIN, null);
        user2.setLogin("login2");
        user2.setEmail("email2@kj.com");
        userDAO.add(user1, connection);
        userDAO.add(user2, connection);

        List<User> result = userDAO.getUsersByRole(Role.STUDENT, connection);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals("login", result.get(0).getLogin());
        Assert.assertEquals("email@i.ua", result.get(0).getEmail());
        Assert.assertEquals(Role.STUDENT, result.get(0).getRole());
    }

    @Test
    public void testGetStudentsByStatus() {
        User user1 = prepareUser(Role.STUDENT, UserStatus.ACTIVE);
        User user2 = prepareUser(Role.ADMIN, UserStatus.BANNED);
        user2.setLogin("login2");
        user2.setEmail("email2@kj.com");
        userDAO.add(user1, connection);
        userDAO.add(user2, connection);

        List<User> result = userDAO.getStudentsByStatus(UserStatus.BANNED, connection);

        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testGetByNameAndSurname() {
        User user = prepareUser(Role.STUDENT, null);
        Integer userId = userDAO.add(user, connection);

        User result = userDAO.getByNameAndSurname("FN", "LN", connection);

        Assert.assertNotNull(result);
        Assert.assertEquals(userId, result.getId());
        Assert.assertEquals("login", result.getLogin());
        Assert.assertEquals("FN", result.getFirstName());
        Assert.assertEquals("LN", result.getLastName());
        Assert.assertEquals(Role.STUDENT, result.getRole());
        Assert.assertEquals(UserStatus.PENDING, result.getStatus());    //Pending is default UserStatus
        Assert.assertEquals("email@i.ua", result.getEmail());

    }

    @Test
    public void testDeleteUser() {
        User testUser = prepareUser(Role.ADMIN, null);
        userDAO.add(testUser, connection);

        boolean deleted = userDAO.deleteById(2020, connection);
        if (testUser.getId() != 2020) {
            Assert.assertFalse(deleted);
        } else {
            Assert.assertTrue(deleted);
        }
    }

    @Test
    public void testDeletePendingUsers() {
        User testUser = prepareUser(Role.ADMIN, UserStatus.PENDING);
        User testUser1 = prepareUser(Role.STUDENT, UserStatus.PENDING);
        testUser1.setLogin("login2");
        testUser1.setEmail("email2@i.com");
        userDAO.add(testUser, connection);
        userDAO.add(testUser1, connection);
        Integer usersInTable = userDAO.getAll(connection).size();

        boolean deleted = userDAO.deletePendingUsers(connection);
        Integer usersInTableNow = userDAO.getAll(connection).size();
        Assert.assertTrue(deleted);
        Assert.assertNotEquals(usersInTable, usersInTableNow);
    }

    @Test
    public void testUpdateUser() {
        User testUser = prepareUser(Role.ADMIN, null);
        userDAO.add(testUser, connection);
        testUser.setLogin("NewLogin");

        boolean updated = userDAO.update(testUser, connection);
        Integer updatedUserId = userDAO.getByLogin("NewLogin", connection).getId();
        Assert.assertTrue(updated);
        Assert.assertEquals(testUser.getId(), updatedUserId);
    }

    @Test
    public void testUpdateStatus() {
        User testUser1 = prepareUser(Role.ADMIN, UserStatus.ACTIVE);
        User testUser2 = prepareUser(Role.ADMIN, UserStatus.PENDING);
        testUser2.setLogin("login2");
        testUser2.setEmail("email2@i.com");
        userDAO.add(testUser1, connection);
        userDAO.add(testUser2, connection);

        boolean updated = userDAO.updateStatus(UserStatus.BANNED, connection, "login", "login2", "login3");
        UserStatus updatedStatus1 = userDAO.getByLogin("login", connection).getStatus();
        UserStatus updatedStatus2 = userDAO.getByLogin("login2", connection).getStatus();

        Assert.assertTrue(updated);
        Assert.assertEquals(UserStatus.BANNED, updatedStatus1);
        Assert.assertEquals(UserStatus.BANNED, updatedStatus2);
    }

    @Test
    public void testChangePassword() {
        User user = prepareUser(Role.STUDENT, UserStatus.ACTIVE);
        userDAO.add(user, connection);
        user.setPassword("newPassword");

        boolean result = userDAO.changePassword(user, connection);
        User updatedUser = userDAO.getById(user.getId(), connection);
        Assert.assertTrue(result);
        Assert.assertEquals("newPassword", updatedUser.getPassword());
    }

    private User prepareUser(Role role, UserStatus status) {
        User testUser = new User();
        testUser.setLogin("login");
        testUser.setPassword("pass");
        testUser.setFirstName("FN");
        testUser.setLastName("LN");
        testUser.setEmail("email@i.ua");
        testUser.setRole(role);
        testUser.setStatus(status);
        return testUser;
    }
}
