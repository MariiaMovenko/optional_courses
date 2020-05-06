package ua.nure.movenko.summaryTask4.services.user;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.nure.movenko.summaryTask4.db.dao.user.UserDAO;
import ua.nure.movenko.summaryTask4.db.transaction.TransactionManagerMock;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.Role;
import ua.nure.movenko.summaryTask4.enums.UserStatus;
import ua.nure.movenko.summaryTask4.models.LectorModel;
import ua.nure.movenko.summaryTask4.models.UserModel;
import ua.nure.movenko.summaryTask4.services.mail.MailService;

import java.sql.Connection;
import java.util.List;


import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private MailService mailService;

    private TransactionManagerMock transactionManager = new TransactionManagerMock();

    @InjectMocks
    private UserService userService = new UserServiceImpl(transactionManager, userDAO, mailService);

    @Test
    public void shouldConfirmRegistration() throws Exception {
        String[] confirmationInfo = {"login", "1"};
        when(userDAO.confirmRegistration(transactionManager.getConnectionForTest(), "login", 1))
                .thenReturn(true);

        userService.confirmRegistration(confirmationInfo);

        transactionManager.verifyExecutedWithoutTransaction();
    }

    @Test
    public void shouldFindUserByLogin() throws Exception {
        User testUser = createUser(Role.STUDENT, UserStatus.ACTIVE);
        when(userDAO.getByLogin("login", transactionManager.getConnectionForTest()))
                .thenReturn(testUser);

        User user = userService.findUserByLogin("login");

        Assert.assertEquals(user.getLogin(), "login");
        Assert.assertEquals(user.getPassword(), "password");
        Assert.assertEquals(user.getEmail(), "test@gmail.com");
        transactionManager.verifyExecutedWithoutTransaction();
    }


    @Test
    public void shouldGetAllLectors() throws Exception {
        User lector = createUser(Role.LECTOR, UserStatus.ACTIVE);
        List<User> lectors = List.of(lector);
        when(userDAO.getUsersByRole(Role.LECTOR, transactionManager.getConnectionForTest()))
                .thenReturn(lectors);

        List<LectorModel> result = userService.getAllLectors();

        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).getEmail(), "test@gmail.com");
        Assert.assertEquals(result.get(0).getLastName(), "Petrov");
        Assert.assertEquals(result.get(0).getFirstName(), "Ivan");
        transactionManager.verifyExecutedWithoutTransaction();
    }

    @Test
    public void shouldGetAllStudents() throws Exception {
        User student = createUser(Role.STUDENT, UserStatus.ACTIVE);
        List<User> students = List.of(student);
        when(userDAO.getUsersByRole(Role.STUDENT, transactionManager.getConnectionForTest()))
                .thenReturn(students);

        List<UserModel> result = userService.getAllStudents();

        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).getLogin(), "login");
        Assert.assertEquals(result.get(0).getEmail(), "test@gmail.com");
        Assert.assertEquals(result.get(0).getFirstName(), "Ivan");
        Assert.assertEquals(result.get(0).getLastName(), "Petrov");
        Assert.assertEquals(result.get(0).getRole(), Role.STUDENT.toString());
        transactionManager.verifyExecutedWithoutTransaction();
    }

    @Test
    public void shouldGeStudentsByStatus() throws Exception {
        User student2 = createUser(Role.STUDENT, UserStatus.PENDING);
        List<User> pendingStudents = List.of(student2);

        when(userDAO.getStudentsByStatus(UserStatus.PENDING, transactionManager.getConnectionForTest()))
                .thenReturn(pendingStudents);
        List<UserModel> result = userService.geStudentsByStatus(UserStatus.PENDING);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).getStatus(), UserStatus.PENDING.toString());
        Assert.assertEquals(result.get(0).getLogin(), "login");
        Assert.assertEquals(result.get(0).getEmail(), "test@gmail.com");
        Assert.assertEquals(result.get(0).getFirstName(), "Ivan");
        Assert.assertEquals(result.get(0).getLastName(), "Petrov");
        Assert.assertEquals(result.get(0).getRole(), Role.STUDENT.toString());
        transactionManager.verifyExecutedWithoutTransaction();
    }

    @Test
    public void shouldAddUser() throws Exception {
        User userToAdd = createUser(Role.LECTOR, UserStatus.BANNED);
        when(userDAO.add(userToAdd, transactionManager.getConnectionForTest()))
                .thenReturn(1);
        doNothing().when(mailService).sendMailWithoutPassw(userToAdd);

        userService.addUser(userToAdd);

        transactionManager.verifyExecutedInTransaction(Connection.TRANSACTION_REPEATABLE_READ);
    }

    @Test
    public void shouldAddUserByAdmin() throws Exception {
        User userToAdd = createUser(Role.STUDENT, UserStatus.BANNED);
        String passwordToSend = userToAdd.getPassword();
        when(userDAO.add(userToAdd, transactionManager.getConnectionForTest()))
                .thenReturn(1);
        doNothing().when(mailService).sendMailWithPassw(userToAdd, passwordToSend);

        userService.addUserByAdmin(userToAdd);

        transactionManager.verifyExecutedInTransaction(Connection.TRANSACTION_REPEATABLE_READ);
    }

    private User createUser(Role role, UserStatus status) {
        User user = new User();
        user.setLogin("login");
        user.setPassword("password");
        user.setId(1);
        user.setRole(role);
        user.setLastName("Petrov");
        user.setFirstName("Ivan");
        user.setEmail("test@gmail.com");
        user.setStatus(status);
        return user;
    }

}
