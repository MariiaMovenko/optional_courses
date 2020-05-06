package ua.nure.movenko.summaryTask4.web.servlets.common;

import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.ValidationStatus;
import ua.nure.movenko.summaryTask4.services.user.UserService;
import ua.nure.movenko.summaryTask4.validation.ValidationBuilder;
import ua.nure.movenko.summaryTask4.validation.ValidationResult;
import ua.nure.movenko.summaryTask4.validation.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * End point of this servlet is to restore the password of the particular user and send him an email with new password
 */
@WebServlet("/restore_password")
public class RestorePasswordServlet extends HttpServlet {
    private UserService userService;
    private Validator validator;

    @Override
    public void init() {
        userService = (UserService) getServletContext().getAttribute("USER_SERVICE");
        validator = new ValidationBuilder()
                .loginFormat()
                .emailFormat()
                .emailMatchesLogin(userService)
                .userNotActive()
                .build();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = request.getParameter(Params.LOGIN);
        ValidationResult validationResult = validator.validate(request);
        if (validationResult.getValidationStatus() == ValidationStatus.SUCCESS) {
            User user = userService.findUserByLogin(login);
            userService.restorePassword(user);
            response.sendRedirect("login.html");
        } else {
            response.sendRedirect("restore_password.html?" + validationResult.toURL());
        }
    }
}
