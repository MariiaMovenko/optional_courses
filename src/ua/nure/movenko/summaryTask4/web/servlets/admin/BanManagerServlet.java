package ua.nure.movenko.summaryTask4.web.servlets.admin;

import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.enums.UserStatus;
import ua.nure.movenko.summaryTask4.services.user.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * End point of this servlet is to ban chosen students
 */
@WebServlet("/ban_manager")
public class BanManagerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserService userService;

    @Override
    public void init() {
        userService = (UserService) getServletContext().getAttribute("USER_SERVICE");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String[] logins = Optional.ofNullable(request.getParameterValues(Params.LOGIN))
                .stream()
                .flatMap(Arrays::stream)
                .filter(login -> login != null && !login.isEmpty())
                .collect(Collectors.toList())
                .toArray(String[]::new);
        UserStatus status = UserStatus.valueOf(request.getParameter(Params.STATUS));
        userService.updateStatus(status, logins);
        Map<String, HttpSession> sessionMap = (HashMap<String, HttpSession>)
                getServletContext().getAttribute("sessionMap");
        for (String login: logins) {
            if (status.equals(UserStatus.BANNED) && sessionMap.containsKey(login)) {
                sessionMap.get(login).invalidate();
                sessionMap.remove(login);
            }
        }
        response.sendRedirect("ban");
    }
}
