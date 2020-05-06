package ua.nure.movenko.summaryTask4.web.servlets.common;

import ua.nure.movenko.summaryTask4.enums.UserStatus;
import ua.nure.movenko.summaryTask4.models.UserModel;
import ua.nure.movenko.summaryTask4.services.user.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *End point of this servlet is to separately retrieve banned and active students
 */
@WebServlet("/ban")
public class GetStudentsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserService userService;

    @Override
    public void init() {
        userService = (UserService) getServletContext().getAttribute("USER_SERVICE");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, List<UserModel>> result = new HashMap<>();
        List<UserModel> students = userService.geStudentsByStatus(UserStatus.ACTIVE);
        List<UserModel> bannedStudents = userService.geStudentsByStatus(UserStatus.BANNED);
        result.put("active", students);
        result.put("banned", bannedStudents);

        request.setAttribute("users", result);
        request.getRequestDispatcher("/ban.html").forward(request, response);
    }
}

