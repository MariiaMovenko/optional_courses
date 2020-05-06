package ua.nure.movenko.summaryTask4.web.servlets.common;

import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.ValidationStatus;
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
 * Updates information on the user in the application
 */
@WebServlet("/update_my_info")
public class UpdateUserServlet extends HttpServlet {

    private UserService userService;
    private Validator validator;

    @Override
    public void init(){
        userService = (UserService) getServletContext().getAttribute("USER_SERVICE");
        validator = new ValidationBuilder()
                .loginFormat()
                .newloginExists(userService)
                .emailFormat()
                .emailExists(userService)
                .build();

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User thisUser = (User) request.getSession().getAttribute(Params.USER_INFO);
        ValidationResult validationResult = validator.validate(request);
        if (validationResult.getValidationStatus() == ValidationStatus.SUCCESS) {
            ParamParser.parseUserExceptPass(request, thisUser);
            userService.updateUser(thisUser);
            response.sendRedirect("main.html");
        } else {
            response.sendRedirect("update_my_info.html?" + validationResult.toURL());
        }
    }
}
