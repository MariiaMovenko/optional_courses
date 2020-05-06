package ua.nure.movenko.summaryTask4.services.user;

import org.apache.commons.fileupload.FileItem;
import ua.nure.movenko.summaryTask4.enums.Role;
import ua.nure.movenko.summaryTask4.exception.SendingMailException;
import ua.nure.movenko.summaryTask4.db.transaction.TransactionManager;
import ua.nure.movenko.summaryTask4.db.dao.user.UserDAO;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.UserStatus;
import ua.nure.movenko.summaryTask4.hash.Password;
import ua.nure.movenko.summaryTask4.models.LectorModel;
import ua.nure.movenko.summaryTask4.models.UserModel;
import ua.nure.movenko.summaryTask4.services.mail.MailService;
import ua.nure.movenko.summaryTask4.util.PasswordGenerator;

import javax.mail.MessagingException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UserServiceImpl based  on implementation of the {@code UserService} interface.
 *
 * @ author M.Movenko
 */
public class UserServiceImpl implements UserService {

    private TransactionManager transactionManager;
    private UserDAO userDAO;
    private MailService mailService;

    public UserServiceImpl(TransactionManager transactionManager, UserDAO userDAO, MailService mailService) {
        this.transactionManager = transactionManager;
        this.userDAO = userDAO;
        this.mailService = mailService;
    }

    @Override
    public void confirmRegistration(String[] confirmationInfo) {
        String login = confirmationInfo[0];
        int userId = Integer.parseInt(confirmationInfo[1]);
        transactionManager.execute(connection -> userDAO
                .confirmRegistration(connection, login, userId), Connection.TRANSACTION_NONE);
    }

    @Override
    public User findUserByLogin(String login) {
        return transactionManager
                .execute(connection -> userDAO.getByLogin(login, connection), Connection.TRANSACTION_NONE);
    }


    @Override
    public List<LectorModel> getAllLectors() {
        return transactionManager.execute(connection -> userDAO.getUsersByRole(Role.LECTOR, connection)
                .stream().map(this::convertToLectorModel)
                .collect(Collectors.toList()), Connection.TRANSACTION_NONE);
    }

    @Override
    public List<UserModel> getAllStudents() {
        return transactionManager.execute(connection -> userDAO.getUsersByRole(Role.STUDENT, connection)
                .stream().map(this::convertToUserModel)
                .collect(Collectors.toList()), Connection.TRANSACTION_NONE);
    }

    @Override
    public List<UserModel> geStudentsByStatus(UserStatus status) {
        return transactionManager.execute(connection -> userDAO.getStudentsByStatus(status, connection)
                .stream().map(this::convertToUserModel)
                .collect(Collectors.toList()), Connection.TRANSACTION_NONE);
    }

    @Override
    public void addUser(User user) {
        transactionManager
                .execute(connection -> addUser(user, connection), Connection.TRANSACTION_REPEATABLE_READ);
    }

    @Override
    public void addUserByAdmin(User user) {
        transactionManager.execute(connection ->
                addUserByAdmin(user, connection), Connection.TRANSACTION_REPEATABLE_READ);
    }

    @Override
    public void updateUser(User user) {
        transactionManager
                .execute(connection -> userDAO.update(user, connection), Connection.TRANSACTION_NONE);
    }

    @Override
    public void changeAvatar(User user) {
        transactionManager
                .execute(connection -> userDAO.changeAvatar(user, connection), Connection.TRANSACTION_NONE);
    }

    @Override
    public void changePassword(User user) {
        transactionManager
                .execute(connection -> userDAO.changePassword(user, connection),
                        Connection.TRANSACTION_NONE);
    }

    @Override
    public void restorePassword(User user) {
        transactionManager.execute(connection -> restorePassword(user, connection),
                Connection.TRANSACTION_REPEATABLE_READ);
    }

    @Override
    public void updateStatus(UserStatus status, String... login) {
        transactionManager.execute(connection -> userDAO.updateStatus(status, connection, login),
                Connection.TRANSACTION_REPEATABLE_READ);
    }

    @Override
    public void deleteUserById(int id) {
        transactionManager.execute(connection ->
                userDAO.deleteById(id, connection), Connection.TRANSACTION_NONE);
    }


    @Override
    public void deletePendingUsers() {
        transactionManager.execute(connection ->
                userDAO.deletePendingUsers(connection), Connection.TRANSACTION_NONE);
    }

    @Override
    public boolean exists(String login) {
        return transactionManager
                .execute(connection -> userDAO.exists(login, connection), Connection.TRANSACTION_NONE);
    }

    @Override
    public boolean emailExists(String email) {
        return transactionManager
                .execute(connection -> userDAO.existsByEmail(email, connection), Connection.TRANSACTION_NONE);
    }

    @Override
    public String uploadFile(FileItem item, User user, String uploadDirectory) throws Exception {
        String  fullPathToFile = null;
            if (!item.isFormField()) {
                File fileSaveDir = new File(uploadDirectory);
                if (!fileSaveDir.exists()) {
                    fileSaveDir.mkdir();
                }
                String name = new File(item.getName()).getName();
                fullPathToFile = uploadDirectory + File.separator + name;
                if (Files.exists(Path.of(fullPathToFile))) {
                    String uniqueLogin = user.getLogin();
                    fullPathToFile = uploadDirectory + File.separator + uniqueLogin + name;
                }
                item.write(new File(fullPathToFile));
        }
        return fullPathToFile;
    }

    private int addUser(User user, Connection connection) {
        int userId = userDAO.add(user, connection);
        user.setId(userId);
        try {
            mailService.sendMailWithoutPassw(user);
            return userId;
        } catch (MessagingException e) {
            throw new SendingMailException(e);
        }
    }

    private int addUserByAdmin(User user, Connection connection) {
        String passwordToSend = user.getPassword();
        user.setPassword(Password.hash(passwordToSend));
        int userId = userDAO.add(user, connection);
        user.setId(userId);
        try {
            mailService.sendMailWithPassw(user, passwordToSend);
            return userId;
        } catch (MessagingException e) {
            throw new SendingMailException(e);
        }
    }

    private boolean restorePassword(User user, Connection connection) {
        String passwordToSend = PasswordGenerator.generate(5);
        user.setPassword(Password.hash(passwordToSend));
        boolean result;
        try {
            result = userDAO.changePassword(user, connection);
            mailService.sendRestoredPassword(user, passwordToSend);
        } catch (MessagingException e) {
            throw new SendingMailException(e);
        }
        return result;
    }

    private LectorModel convertToLectorModel(User user) {
        LectorModel lector = new LectorModel();
        lector.setLectorId(user.getId());
        lector.setFirstName(user.getFirstName());
        lector.setLastName(user.getLastName());
        lector.setEmail(user.getEmail());
        return lector;
    }

    private UserModel convertToUserModel(User user) {
        UserModel model = new UserModel();
        model.setLogin(user.getLogin());
        model.setStatus(user.getStatus().toString());
        model.setFirstName(user.getFirstName());
        model.setLastName(user.getLastName());
        model.setEmail(user.getEmail());
        model.setRole(user.getRole().toString());
        model.setAvatarPath(user.getAvatarPath());
        return model;
    }
}
