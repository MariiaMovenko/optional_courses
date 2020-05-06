package ua.nure.movenko.summaryTask4.web.servlets.admin;

import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.ValidationStatus;
import ua.nure.movenko.summaryTask4.util.PasswordGenerator;
import ua.nure.movenko.summaryTask4.services.user.UserService;
import ua.nure.movenko.summaryTask4.util.ParamParser;
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
 * Registers users to OptionalCourses web-site and sends them emails with their credentials
 */
@WebServlet("/register_somebody")
public class RegistrationByAdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;
    private Validator validator;

    @Override
    public void init() {
        userService = (UserService) getServletContext().getAttribute("USER_SERVICE");
        validator = new ValidationBuilder()
                .loginFormat()
                .loginExists(userService, false)
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
            int passwordLength = 5;
            String generetedPassword = PasswordGenerator.generate(passwordLength);
            user.setPassword(generetedPassword);
            userService.addUserByAdmin(user);
            response.sendRedirect("main.html");
        } else {
            response.sendRedirect("register-somebody.html?" + validationResult.toURL());
        }
    }

}
