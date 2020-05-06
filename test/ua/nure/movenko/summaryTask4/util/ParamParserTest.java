package ua.nure.movenko.summaryTask4.util;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.entities.Course;
import ua.nure.movenko.summaryTask4.entities.Theme;
import ua.nure.movenko.summaryTask4.entities.User;

import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ParamParserTest {

    private static final RequestMock request = new RequestMock();

    @BeforeClass
    public static void setUpClass() {
        Map<String, String[]> paramMap = new HashMap<>();
        paramMap.put(Params.ID, new String[]{"5"});
        paramMap.put(Params.THEME_TITLE, new String[]{"Subject Title"});
        paramMap.put(Params.EN_THEME_TITLE, new String[]{"EnSubject"});
        paramMap.put(Params.RU_THEME_TITLE, new String[]{"RuSubject"});
        paramMap.put(Params.EN_COURSE_TITLE, new String[]{"EnTitle"});
        paramMap.put(Params.RU_COURSE_TITLE, new String[]{"RuTitle"});
        paramMap.put(Params.LECTOR_INFO, new String[]{"Petr Petrov"});
        paramMap.put(Params.START_DATE, new String[]{"2020-10-10"});
        paramMap.put(Params.FINISH_DATE, new String[]{"2020-12-12"});
        paramMap.put(Params.LOGIN, new String[]{"login"});
        paramMap.put(Params.FIRST_NAME, new String[]{"FirstName"});
        paramMap.put(Params.LAST_NAME, new String[]{"LastName"});
        paramMap.put(Params.PATH_TO_AVATAR, new String[]{"C:\\actualPath\\123.jpg"});
        paramMap.put(Params.ROLE, new String[]{null});
        paramMap.put(Params.EMAIL, new String[]{"email@gmail.com"});
        paramMap.put(Params.LIMIT, new String[]{null});
        paramMap.put(Params.PAGE, new String[]{"2"});
        paramMap.put(Params.OPERATION, new String[]{"filter"});
        paramMap.put(Params.SORT_ORDER, new String[]{null});
        paramMap.put(Params.SORT_CRITERIA, new String[]{null});
        request.setParamMap(paramMap);
        request.getSession().setAttribute(Params.LANGUAGE, "ruS");
        request.getSession().setAttribute(Params.LIMIT, null);
        request.getSession().setAttribute(Params.USER_INFO, null);
    }

    @Test
    public void shouldParseCourse() {
        Course testCourse = ParamParser.parseCourse(request);

        Assert.assertNotNull(testCourse);
        Assert.assertEquals(testCourse.getTitleDictionary().getRu(), "RuTitle");
        Assert.assertEquals(testCourse.getTheme().getThemeTitleEn(), "Subject Title");
        Assert.assertEquals(testCourse.getLector().getFirstName(), "Petr");
        Assert.assertEquals(testCourse.getLector().getLastName(), "Petrov");
    }

    @Test
    public void shouldParseUserExceptPass() {
        User testUser = new User();
        ParamParser.parseUserExceptPass(request, testUser);

        Assert.assertEquals(testUser.getLogin(), "login");
        Assert.assertEquals(testUser.getFirstName(), "FirstName");
        Assert.assertEquals(testUser.getLastName(), "LastName");
        Assert.assertEquals(testUser.getRole().getName(), "student");
    }

    @Test
    public void shouldParseTheme() {
        Theme theme = ParamParser.parseTheme(request);

        Assert.assertNotNull(theme);
        Assert.assertEquals(theme.getThemeTitleRu(), "RuSubject");
        Assert.assertEquals(theme.getThemeTitleEn(), "EnSubject");
    }

    @Test
    public void shouldParseConfigParams() {
        ViewConfiguration configuration = ParamParser.parseViewParams(request);

        Assert.assertNotNull(configuration);
        Assert.assertEquals(configuration.getLanguage(), "ru");
        Assert.assertEquals(configuration.getLimit(), Integer.valueOf(3));
        Assert.assertEquals(configuration.getOffset(), Integer.valueOf(3));
        Assert.assertEquals(configuration.getOperation(), "filter");
        Assert.assertNull(configuration.getCriteria(), null);
        Assert.assertNull(configuration.getOrder(), null);
    }
}

