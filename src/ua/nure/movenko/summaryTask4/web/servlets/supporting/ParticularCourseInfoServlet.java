package ua.nure.movenko.summaryTask4.web.servlets.supporting;

import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.entities.Course;
import ua.nure.movenko.summaryTask4.services.course.CourseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This servlet retrieves information on the particular course
 * and conveys it to anothe servlet for further processing.
 */
@WebServlet("/course_info")
public class ParticularCourseInfoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CourseService courseService;

    @Override
    public void init() {
        courseService = (CourseService) getServletContext().getAttribute("COURSE_SERVICE");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Integer courseId = Integer.parseInt(request.getParameter(Params.ID));
        Course course = courseService.getCourseById(courseId);
        request.setAttribute("CourseInfo", course);
        request.getRequestDispatcher("update-course.html").forward(request, response);
    }
}
