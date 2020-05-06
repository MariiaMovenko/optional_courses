package ua.nure.movenko.summaryTask4.web.servlets.supporting;

import com.google.gson.Gson;
import ua.nure.movenko.summaryTask4.models.CourseModel;
import ua.nure.movenko.summaryTask4.services.course.CourseService;

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
 * Returns the quantity of all existing courses.
 */
@WebServlet("/courses_count")
public class CourseCountServlet extends HttpServlet {

    private CourseService courseService;

    @Override
    public void init() {
        courseService = (CourseService) getServletContext().getAttribute("COURSE_SERVICE");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int coursesCount = courseService.countCourses();
        Map<String, Integer> responseMap = new HashMap<>();
        responseMap.put("count", coursesCount);
        String json = new Gson().toJson(responseMap);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }
}
