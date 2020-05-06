package ua.nure.movenko.summaryTask4.web.servlets.common;

import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.ValidationStatus;
import ua.nure.movenko.summaryTask4.services.user.UserService;
import ua.nure.movenko.summaryTask4.validation.ValidationBuilder;
import ua.nure.movenko.summaryTask4.validation.ValidationResult;
import ua.nure.movenko.summaryTask4.validation.Validator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * Uses for Log users in
 */
@WebServlet(value = "/login", loadOnStartup = 1)
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Map<String, HttpSession> sessionMap;
    private Validator validator;

    @Override
    public void init() {
        UserService userService = (UserService) getServletContext().getAttribute("USER_SERVICE");
        sessionMap = (Map<String, HttpSession>) getServletContext().getAttribute("sessionMap");
        validator = new ValidationBuilder()
                .loginFormat()
                .loginExists(userService, true)
                .passwordFormat(Params.PASSWORD, false)
                .passwordMatchesLogin(userService)
                .userNotActive()
                .build();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String login = request.getParameter(Params.LOGIN);
        ValidationResult validationResult = validator.validate(request);
        if (validationResult.getValidationStatus() == ValidationStatus.SUCCESS) {
            User validatedUser = (User) request.getAttribute(Params.USER_INFO);
            HttpSession session = request.getSession();
            session.setAttribute(Params.USER_INFO, validatedUser);
            sessionMap.put(login, session);
            response.sendRedirect("main.html");
        } else {
            response.sendRedirect("login.html?" + validationResult.toURL());
        }
    }
}

