package ua.nure.movenko.summaryTask4.web.servlets.common;

import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.entities.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * This servlet ends session and logs user out
 */
@WebServlet(value = "/logout", loadOnStartup = 1)
public class LogoutServlet extends HttpServlet {

    private Map<String, HttpSession> sessionMap;

    @Override
    public void init() {
        sessionMap = (Map<String, HttpSession>) getServletContext().getAttribute("sessionMap");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Params.USER_INFO);
        sessionMap.remove(user.getLogin(),session);
        session.invalidate();
        response.sendRedirect("main.html");
    }

}
