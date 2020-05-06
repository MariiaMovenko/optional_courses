package ua.nure.movenko.summaryTask4.web.servlets.admin;

import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.services.course.CourseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * End point of this servlet is to delete a particular course
 */
@WebServlet("/delete_course")
public class CourseRemovingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CourseService courseService;


    @Override
    public void init() {
        courseService = (CourseService) getServletContext().getAttribute("COURSE_SERVICE");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String title = request.getParameter(Params.TITLE);
        courseService.deleteCourse(title);
    }
}
