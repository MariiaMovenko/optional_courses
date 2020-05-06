package ua.nure.movenko.summaryTask4.web.servlets.common;

import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.ValidationStatus;
import ua.nure.movenko.summaryTask4.hash.Password;
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
 * End point of this servlet is to change user's password
 */
@WebServlet("/change_password")
public class ChangePasswordServlet extends HttpServlet {

    private UserService userService;
    private Validator validator;

    @Override
    public void init() {
        userService = (UserService) getServletContext().getAttribute("USER_SERVICE");
        validator = new ValidationBuilder()
                .passwordFormat(Params.CURRENT_PASSWORD, false)
                .checkCurrentPassword()
                .passwordFormat(Params.NEW_PASSWORD, false)
                .checkConfirmingPasswords(Params.NEW_PASSWORD)
                .build();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User thisUser = (User) request.getSession().getAttribute(Params.USER_INFO);
        String newPassword = Password.hash(request.getParameter(Params.NEW_PASSWORD));
        ValidationResult validationResult = validator.validate(request);
        if (validationResult.getValidationStatus() == ValidationStatus.SUCCESS) {
            thisUser.setPassword(newPassword);
            userService.changePassword(thisUser);
            response.sendRedirect("main.html");
        } else {
            response.sendRedirect("change_password.html?" + validationResult.toURL());
        }
    }
}
