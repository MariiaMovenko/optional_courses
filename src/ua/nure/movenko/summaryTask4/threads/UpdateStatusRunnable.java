package ua.nure.movenko.summaryTask4.threads;

import ua.nure.movenko.summaryTask4.services.user.UserService;

/**
 * UpdateStatusRunnable is an  implementation of {@code Runnable} interface; It's assignment is to remove
 * from Database recently registered users who did not confirm the registration during particular period of time.
 *
 * @author M.Movenko
 */
public class UpdateStatusRunnable implements Runnable {

    UserService userService;

    public UpdateStatusRunnable(UserService userService) {
        this.userService = userService;
    }

    /**
     * Removes from Database recently registered users who did not confirm the registration
     * during particular period of time.
     */
    @Override
    public void run() {
        userService.deletePendingUsers();
    }
}
