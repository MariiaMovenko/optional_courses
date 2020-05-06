package ua.nure.movenko.summaryTask4.web.servlets.common;

import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.ValidationStatus;
import ua.nure.movenko.summaryTask4.hash.Password;
import ua.nure.movenko.summaryTask4.services.user.UserService;
import ua.nure.movenko.summaryTask4.util.ParamParser;
import ua.nure.movenko.summaryTask4.validation.ValidationBuilder;
import ua.nure.movenko.summaryTask4.validation.ValidationResult;
import ua.nure.movenko.summaryTask4.validation.Validator;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Registers users at OptionalCourses web-site as Students
 */
@WebServlet("/registration")
public class StudentRegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;
    private Validator validator;

    @Override
    public void init() {
        userService = (UserService) getServletContext().getAttribute("USER_SERVICE");
        validator = new ValidationBuilder()
                .loginFormat()
                .loginExists(userService, false)
                .passwordFormat(Params.PASSWORD, true)
                .checkConfirmingPasswords(Params.PASSWORD)
                .emailFormat()
                .emailExists(userService)
                .build();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ValidationResult validationResult = validator.validate(request);
        if (validationResult.getValidationStatus() == ValidationStatus.SUCCESS) {
            User user = new User();
            ParamParser.parseUserExceptPass(request, user);
            user.setPassword(Password.hash(request.getParameter(Params.PASSWORD)));
            userService.addUser(user);
            response.sendRedirect("success_registration.html");
        } else {
            response.sendRedirect("registration.html?" + validationResult.toURL());
        }
    }
}
