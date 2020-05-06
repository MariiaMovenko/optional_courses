package ua.nure.movenko.summaryTask4.web.servlets.student;

import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.CourseStatus;
import ua.nure.movenko.summaryTask4.models.StudentCourseModel;
import ua.nure.movenko.summaryTask4.services.course.CourseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * End point of this servlet is to retrieve all courses the particular student is enrolled to
 */
@WebServlet("/student_courses")
public class StudentCoursesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CourseService courseService;

    @Override
    public void init() {
        courseService = (CourseService) getServletContext().getAttribute("COURSE_SERVICE");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String language = Optional.ofNullable(request.getParameter(Params.LANGUAGE))
                .map(locale -> locale.substring(0, 2))
                .orElse(getLocaleFromSession(request.getSession()));

        User student = (User) request.getSession().getAttribute(Params.USER_INFO);

        List<StudentCourseModel> pendingCourses = courseService.getStudentCourses(student,
                CourseStatus.PENDING, language);
        List<StudentCourseModel> coursesInProgress = courseService.getStudentCourses(student,
                CourseStatus.IN_PROGRESS, language);
        List<StudentCourseModel> finishedCourses = courseService.getStudentCourses(student,
                CourseStatus.FINISHED, language);
        Map<String, List<StudentCourseModel>> results = new HashMap<>();
        results.put("pending", pendingCourses);
        results.put("inProgress", coursesInProgress);
        results.put("finished", finishedCourses);

        request.setAttribute("courses", results);
        request.getRequestDispatcher("/student-courses.html").forward(request, response);

    }

    private String getLocaleFromSession(HttpSession session) {
        return Optional.ofNullable(session)
                .map(httpSession -> httpSession.getAttribute(Params.LANGUAGE))
                .map(Object::toString)
                .map(locale -> locale.substring(0, 2))
                .orElse("ru");
    }

}
