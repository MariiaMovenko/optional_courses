package ua.nure.movenko.summaryTask4.services.user;

import org.apache.commons.fileupload.FileItem;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.UserStatus;
import ua.nure.movenko.summaryTask4.models.LectorModel;
import ua.nure.movenko.summaryTask4.models.UserModel;

import java.util.List;

public interface UserService {

    /**
     * Changes actual {@code UserStatus.PENDING} of particular user to {@code UserStatus.ACTIVE}
     *
     * @param confirmationInfo represents information obtained from  user
     */
    void confirmRegistration(String[] confirmationInfo);

    /**
     * Returns user with the specified login
     *
     * @param login is criteria to find the user by
     * @return {@code null} if there no users with the specified {@code login}
     */
    User findUserByLogin(String login);

    /**
     * Returns all adapted to view layer information on Users whose role  {@code equals(Role.LECTOR)}
     *
     * @return the list of models that represents adapted to view layer information on all lectors
     */
    List<LectorModel> getAllLectors();

    /**
     * Returns all adapted to view layer information on Users whose role  {@code equals(Role.STUDENT)}
     *
     * @return the list of models that represents adapted to view layer information on all students
     */
    List<UserModel> getAllStudents();

    /**
     * Returns  list of models of all users whose role is (@equals(Role.STUDENT)) and status is {@code status}
     *
     * @param status is criteria to find users by
     * @return the list of  {@code UserModel} objects
     */
    List<UserModel> geStudentsByStatus(UserStatus status);

    /**
     * Method registers  new {@code user} in the system sending to this user an email  with identical link for
     * confirming the registration
     *
     * @param user an object to save
     */
    void addUser(User user);

    /**
     * Registers  new {@code user} in the system sending to this user an email  with identical password
     * and link dor confirming the registration
     *
     * @param user an object to save
     */
    void addUserByAdmin(User user);

    /**
     * Updates  information on the {@code user}
     *
     * @param user contains parameters to  update
     */
    void updateUser(User user);

    /**
     * Updates  information on the user's avatar photo by changing actual path to it
     *
     * @param user contains parameters to  update
     */
    void changeAvatar(User user);

    /**
     * Updates user's password
     *
     * @param user user whose password should be changed, {@code user.getPassword()} represents up-to-date password
     */
    void changePassword(User user);

    /**
     * Sends an email with new password to the {@code user}
     *
     * @param user whose password should be restored
     */
    void restorePassword(User user);

    /**
     * Updates actual status of user  with {@code status}
     *
     * @param status defines actual status of user with {@code login} to be changed to
     * @param login  defines user(s) whose status should be changed to {@code status}
     */
    void updateStatus(UserStatus status, String... login);

    /**
     * Removes user from the system  by  it's {@code id}
     *
     * @param id defines the user  to be deleted
     */
    void deleteUserById(int id);

    /**
     * Removes all users with {@code UserStatus.Pending}
     */
    void deletePendingUsers();

    /**
     * Check if  particular {@code login} is already registered in the system
     *
     * @param login of user
     * @return {@code true} if the specified login exists
     */
    boolean exists(String login);

    /**
     * Check if  particular {@code email} exists
     *
     * @param email user's email
     * @return {@code true} if the specified email exists
     */
    boolean emailExists(String email);

    /**
     * Saves multipart item to the particular directory
     *
     * @param item             to be saved
     * @param user             who uploads  {@code item} as an avatar
     * @param upload_directory is directory items to be saved
     * @return full path to the item
     * @throws Exception if cannot write uploaded file to disk!
     */
    String uploadFile(FileItem item, User user, String upload_directory) throws Exception;
}

