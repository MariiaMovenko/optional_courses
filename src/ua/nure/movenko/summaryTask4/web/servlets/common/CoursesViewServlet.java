package ua.nure.movenko.summaryTask4.web.servlets.common;

import com.google.gson.Gson;
import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.models.CourseModel;
import ua.nure.movenko.summaryTask4.services.course.CourseService;
import ua.nure.movenko.summaryTask4.services.theme.ThemeService;
import ua.nure.movenko.summaryTask4.services.user.UserService;
import ua.nure.movenko.summaryTask4.util.ParamParser;
import ua.nure.movenko.summaryTask4.util.ViewConfiguration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Retrieves either all existing courses  ordered by particular parameter or
 *  some courses after using theme and/or lector filters
 */
@WebServlet("/show_courses")
public class CoursesViewServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    CourseService courseService;
    ThemeService themeService;
    UserService userService;

    @Override
    public void init() {
        courseService = (CourseService) getServletContext().getAttribute("COURSE_SERVICE");
        themeService = (ThemeService) getServletContext().getAttribute("THEME_SERVICE");
        userService = (UserService) getServletContext().getAttribute("USER_SERVICE");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ViewConfiguration paramsEntity = ParamParser.parseViewParams(request);
        List<CourseModel> courses;
        if ("filter".equalsIgnoreCase(paramsEntity.getOperation())) {
            String themeTitle = Optional.ofNullable(request.getParameter(Params.THEME_TITLE))
                    .filter(title -> !title.isEmpty()).orElse(null);
            String lectorSurname = Optional.ofNullable(request.getParameter(Params.LECTOR_INFO))
                    .filter(name -> !name.isEmpty())
                    .map(name -> name.split(" ")[1])
                    .orElse(null);
            courses = chooseFilter(paramsEntity, themeTitle, lectorSurname);
        } else {
            courses = courseService.getAllCoursesOrdered(paramsEntity);
        }
        String json = new Gson().toJson(courses);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }


    private List<CourseModel> chooseFilter(ViewConfiguration paramsEntity,
                                           String themeTitle, String lectorSurname) {

        if (themeTitle != null && lectorSurname == null) {
            return courseService.getCoursesByTheme(paramsEntity, themeTitle);
        } else if (themeTitle == null && lectorSurname != null) {
            return courseService.getCoursesByLectorSurname(paramsEntity, lectorSurname);
        } else if (lectorSurname != null) {
            return courseService.getCoursesByThemeAndLector(paramsEntity, themeTitle, lectorSurname);
        } else {
            return courseService.getAllCoursesOrdered(paramsEntity);
        }
    }
}


