package ua.nure.movenko.summaryTask4.web.servlets.common;

import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.services.user.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * End point of this servlet is to activate recently registered account
 */
@WebServlet("/confirm_registration")
public class ConfirmRegistrationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserService userService;

    @Override
    public void init() {
        userService = (UserService) getServletContext().getAttribute("USER_SERVICE");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String[] confirmationInfo = request.getParameter(Params.CONFIRMATION_CODE).split("_");
        userService.confirmRegistration(confirmationInfo);
        response.sendRedirect("login.html");
    }
}
