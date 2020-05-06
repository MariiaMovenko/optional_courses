package ua.nure.movenko.summaryTask4.util;

import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.entities.Course;
import ua.nure.movenko.summaryTask4.entities.Theme;
import ua.nure.movenko.summaryTask4.entities.TitleDictionary;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.Role;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.Optional;

/**
 * Util class, it contains methods for building preassigned entities
 * by parsing {@code HttpServletRequest request} parameters.
 *
 * @author M.Movenko.
 */
public final class ParamParser {

    private ParamParser() {
    }

    public static Course parseCourse(HttpServletRequest request) {
        Integer id = request.getParameter(Params.ID) != null ? Integer.parseInt(request.getParameter(Params.ID)) : null;
        String themeTitle = request.getParameter(Params.THEME_TITLE);
        String titleEn = request.getParameter(Params.EN_COURSE_TITLE);
        String titleRu = request.getParameter(Params.RU_COURSE_TITLE);
        String lectorFirstName = request.getParameter(Params.LECTOR_INFO).split(" ")[0];
        String lectorLastName = request.getParameter(Params.LECTOR_INFO).split(" ")[1];
        Date startDate = Date.valueOf(request.getParameter(Params.START_DATE));
        Date finishDate = Date.valueOf(request.getParameter(Params.FINISH_DATE));
        String description = request.getParameter(Params.DESCRIPTION);
        Course course = new Course();
        course.setId(id);
        course.setTitleDictionary(new TitleDictionary(titleEn, titleRu));
        course.setTheme(new Theme(themeTitle, themeTitle));
        User lector = new User();
        lector.setFirstName(lectorFirstName);
        lector.setLastName(lectorLastName);
        course.setLector(lector);
        course.setStartDate(startDate);
        course.setFinishDate(finishDate);
        course.setDescription(description);
        return course;
    }

    public static void parseUserExceptPass(HttpServletRequest request, User user) {
        user.setLogin(request.getParameter(Params.LOGIN));
        user.setFirstName(request.getParameter(Params.FIRST_NAME));
        user.setLastName(request.getParameter(Params.LAST_NAME));
        user.setEmail(request.getParameter(Params.EMAIL));
        Role role = request.getParameter(Params.ROLE) == null ?
                Role.STUDENT : Role.valueOf(request.getParameter(Params.ROLE));
        user.setRole(role);
        user.setAvatarPath(request.getParameter(Params.PATH_TO_AVATAR));
    }

    public static Theme parseTheme(HttpServletRequest request) {
        String ruTheme = request.getParameter(Params.RU_THEME_TITLE);
        String enTheme = request.getParameter(Params.EN_THEME_TITLE);
        return new Theme(enTheme, ruTheme);
    }

    public static ViewConfiguration parseViewParams(HttpServletRequest request) {
        int defaultPage = 1;
        int defaultId = -1;
        Integer limit = (request.getParameter(Params.LIMIT) != null) ?
                Integer.parseInt(request.getParameter(Params.LIMIT)) :
                (Integer) request.getSession().getAttribute(Params.LIMIT);
        request.getSession().setAttribute(Params.LIMIT, limit);
        int pageNumber = (request.getParameter(Params.PAGE) != null) ?
                Integer.parseInt(request.getParameter(Params.PAGE)) : defaultPage;
        int offset = limit * (pageNumber - 1);

        ViewConfiguration paramsEntity = new ViewConfiguration();
        paramsEntity.setLimit(limit);
        paramsEntity.setOffset(offset);
        paramsEntity.setLanguage(Optional.ofNullable(request.getSession())
                .map(session -> session.getAttribute(Params.LANGUAGE))
                .map(Object::toString)
                .map(locale -> locale.substring(0, 2))
                .orElse("ru"));
        paramsEntity.setOperation(request.getParameter(Params.OPERATION));
        paramsEntity.setUserId((request.getSession().getAttribute(Params.USER_INFO) != null) ?
                (((User) request.getSession().getAttribute(Params.USER_INFO)).getId()) : defaultId);
        paramsEntity.setCriteria(request.getParameter(Params.SORT_CRITERIA));
        paramsEntity.setOrder(request.getParameter(Params.SORT_ORDER));
        return paramsEntity;
    }
}
