package ua.nure.movenko.summaryTask4.web.servlets.admin;

import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.entities.Course;
import ua.nure.movenko.summaryTask4.enums.ValidationStatus;
import ua.nure.movenko.summaryTask4.services.course.CourseService;
import ua.nure.movenko.summaryTask4.services.dictionary.TitleDictionaryService;
import ua.nure.movenko.summaryTask4.util.ParamParser;
import ua.nure.movenko.summaryTask4.validation.ValidationBuilder;
import ua.nure.movenko.summaryTask4.validation.ValidationResult;
import ua.nure.movenko.summaryTask4.validation.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * End point of this servlet is to update information on the particular course
 */
@WebServlet("/update_course")
public class CourseUpdatingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CourseService courseService;
    private TitleDictionaryService dictionaryService;
    private Validator validator;

    @Override
    public void init() {
        courseService = (CourseService) getServletContext().getAttribute("COURSE_SERVICE");
        dictionaryService = (TitleDictionaryService) getServletContext().getAttribute("DICTIONARY_SERVICE");
        validator = new ValidationBuilder()
                .dateFormat(Params.START_DATE)
                .dateFormat(Params.FINISH_DATE)
                .titleFormat(Params.EN_COURSE_TITLE, false)
                .titleFormat(Params.RU_COURSE_TITLE, false)
                .changedTitleExists(dictionaryService, Params.EN_COURSE_TITLE)
                .changedTitleExists(dictionaryService, Params.RU_COURSE_TITLE)
                .build();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        ValidationResult validationResult = validator.validate(request);
        if (validationResult.getValidationStatus() == ValidationStatus.SUCCESS) {
            Course course = ParamParser.parseCourse(request);
            courseService.updateCourse(course);
            response.sendRedirect("courses.html");
        } else {
            response.sendRedirect("update-course.html?" + validationResult.toURL());
        }
    }
}
