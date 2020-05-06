package ua.nure.movenko.summaryTask4.validation;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.UserStatus;
import ua.nure.movenko.summaryTask4.enums.ValidationStatus;
import ua.nure.movenko.summaryTask4.hash.Password;
import ua.nure.movenko.summaryTask4.services.course.CourseService;
import ua.nure.movenko.summaryTask4.services.dictionary.TitleDictionaryService;
import ua.nure.movenko.summaryTask4.services.theme.ThemeService;
import ua.nure.movenko.summaryTask4.services.user.UserService;
import ua.nure.movenko.summaryTask4.util.RequestMock;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValidationTest {

    private static RequestMock requestMock;
    private static Map<String, String[]> paramMap;

    @Mock
    UserService userService;

    @Mock
    CourseService courseService;

    @Mock
    TitleDictionaryService dictionaryService;

    @Mock
    ThemeService themeService;

    @BeforeClass
    public static void setUp() {
        requestMock = new RequestMock();
        paramMap = new HashMap<>();
        Map<String, Object> attrMap = new HashMap<>();
        requestMock.setParamMap(paramMap);
        requestMock.setAttrMap(attrMap);
    }

    @Test
    public void shouldCheckLoginFormat1() {
        paramMap.put("login", new String[]{"asf074t6fF"});

        Validator validator = new ValidationBuilder().loginFormat().build();

        ValidationResult result = validator.validate(requestMock);
        Assert.assertEquals(result.getValidationStatus(), ValidationStatus.SUCCESS);
        Assert.assertEquals(result.toURL(), "");
    }

    @Test
    public void shouldCheckLoginFormat2() {
        paramMap.put("login", new String[]{"/.asf074t6f F"});

        Validator validator = new ValidationBuilder().loginFormat().build();

        ValidationResult result = validator.validate(requestMock);
        Assert.assertEquals(result.getValidationStatus(), ValidationStatus.FAILED);
        Assert.assertEquals(result.toURL(), "login_error=wrong_login_format");
    }

    @Test
    public void shouldCheckTitleFormat() {
        paramMap.put("titleFail", new String[]{" testTitle"});
        paramMap.put("titleOk", new String[]{"New Normal. Title*"});

        Validator validator = new ValidationBuilder()
                .titleFormat("titleFail", true)
                .titleFormat("titleOk", true)
                .build();
        ValidationResult result = validator.validate(requestMock);

        Assert.assertEquals(ValidationStatus.FAILED, result.getValidationStatus());
        Assert.assertEquals("titleFail_error=wrong_title_format", result.toURL());

    }

    @Test
    public void shouldCheckTruePasswordFormat() {
        paramMap.put("password", new String[]{"o3gtI8f"});

        Validator validator = new ValidationBuilder().passwordFormat("password", false).build();

        ValidationResult result = validator.validate(requestMock);
        Assert.assertEquals(result.getValidationStatus(), ValidationStatus.SUCCESS);
        Assert.assertEquals(result.toURL(), "");
    }

    @Test
    public void shouldCheckFalsePasswordFormat() {
        paramMap.put("password", new String[]{"o.*"});

        Validator validator = new ValidationBuilder().passwordFormat("password", true).build();

        ValidationResult result = validator.validate(requestMock);
        Assert.assertEquals(result.getValidationStatus(), ValidationStatus.FAILED);
        Assert.assertEquals(result.toURL(), "password_error=wrong_password_format");
    }

    @Test
    public void shouldCheckTrueEmailFormat() {
        paramMap.put("email", new String[]{"yedwvcjsdnudbskvc@iii.oigvf"});

        Validator validator = new ValidationBuilder().emailFormat().build();

        ValidationResult result = validator.validate(requestMock);
        Assert.assertEquals(result.getValidationStatus(), ValidationStatus.SUCCESS);
        Assert.assertEquals(result.toURL(), "");
    }

    @Test
    public void shouldCheckFalseEmailFormat() {
        paramMap.put("email", new String[]{"ydwvcjsdniii.oigN"});

        Validator validator = new ValidationBuilder().emailFormat().build();

        ValidationResult result = validator.validate(requestMock);
        Assert.assertEquals(result.getValidationStatus(), ValidationStatus.FAILED);
        Assert.assertEquals(result.toURL(), "email_error=wrong_email_format");
    }

    @Test
    public void testTitleExistValidation() {
        paramMap.put("title1", new String[]{"title1"});
        when(courseService.exists("title1")).thenReturn(false, true);

        Validator validator = new ValidationBuilder()
                .titleExists(courseService, "title1")
                .titleExists(courseService, "title1")
                .build();
        ValidationResult result = validator.validate(requestMock);

        Assert.assertEquals(ValidationStatus.FAILED, result.getValidationStatus());
        Assert.assertEquals("title1_error=title_exists", result.toURL());
    }

    @Test
    public void testChangedTitleExistValidation() {
        paramMap.put("title0", new String[]{"title0"});
        paramMap.put("id", new String[]{"0"});
        String title = requestMock.getParameter("title0");
        int courseId = Integer.parseInt(requestMock.getParameter("id"));
        when(dictionaryService.titleAlreadyInUsage(title, courseId)).thenReturn(false);

        Validator validator = new ValidationBuilder()
                .changedTitleExists(dictionaryService, "title0")
                .build();
        ValidationResult result = validator.validate(requestMock);

        Assert.assertEquals(ValidationStatus.SUCCESS, result.getValidationStatus());
        Assert.assertEquals("", result.toURL());
    }

    @Test
    public void testDateFormatValidation() {
        paramMap.put("date", new String[]{"2020-10-09"});
        paramMap.put("date1", new String[]{"03.10.2020"});
        paramMap.put("date2", new String[]{"03-10-2020"});

        Validator validator = new ValidationBuilder().dateFormat("date")
                .dateFormat("date1")
                .dateFormat("date2")
                .build();
        ValidationResult result = validator.validate(requestMock);

        Assert.assertEquals(ValidationStatus.FAILED, result.getValidationStatus());
        Assert.assertEquals("date1_error=wrong_date_format&date2_error=wrong_date_format", result.toURL());
    }

    @Test
    public void testNewLoginExistValidation() {
        User user = new User();
        user.setLogin("login000");
        paramMap.put("login", new String[]{"login"});
        requestMock.getSession().setAttribute("userInfo", user);
        when(userService.exists(requestMock.getParameter("login"))).thenReturn(true);

        Validator validator = new ValidationBuilder().newloginExists(userService).build();
        ValidationResult result = validator.validate(requestMock);

        Assert.assertEquals(ValidationStatus.FAILED, result.getValidationStatus());
        Assert.assertEquals("login_error=login_exists", result.toURL());
    }

    @Test
    public void testThemeExistValidation() {
        paramMap.put("theme", new String[]{"ThemeTitle"});
        when(themeService.exists(requestMock.getParameter("theme"))).thenReturn(false, true);

        Validator validator = new ValidationBuilder()
                .themeExists(themeService, "theme")
                .themeExists(themeService, "theme")
                .build();
        ValidationResult validationResult = validator.validate(requestMock);

        Assert.assertEquals(ValidationStatus.FAILED, validationResult.getValidationStatus());
        Assert.assertEquals("theme_error=theme_exists", validationResult.toURL());
    }

    @Test
    public void checkIfLoginExists() {
        paramMap.put("login", new String[]{"TestLogin"});
        when(userService.exists(requestMock.getParameter("login"))).thenReturn(true);

        Validator validator = new ValidationBuilder()
                .loginExists(userService, true)
                .loginExists(userService, false)
                .build();
        ValidationResult result = validator.validate(requestMock);

        Assert.assertEquals(result.getValidationStatus(), ValidationStatus.FAILED);
        Assert.assertEquals(result.toURL(), "login_error=login_exists");
    }

    @Test
    public void checkIfEmailExists() {
        paramMap.put("email", new String[]{"TestEmail"});
        when(userService.emailExists(requestMock.getParameter("email"))).thenReturn(true);

        Validator validator = new ValidationBuilder().emailExists(userService).build();
        ValidationResult result = validator.validate(requestMock);

        Assert.assertEquals(result.getValidationStatus(), ValidationStatus.FAILED);
        Assert.assertEquals(result.toURL(), "email_error=email_exists");

    }

    @Test
    public void shouldCheckCurrentPassword() {
        paramMap.put("current_password", new String[]{"password"});
        User testUser = new User();
        testUser.setPassword(Password.hash("password"));
        requestMock.getSession().setAttribute("userInfo", testUser);

        Validator validator = new ValidationBuilder().checkCurrentPassword().build();
        ValidationResult result = validator.validate(requestMock);

        Assert.assertEquals(result.getValidationStatus(), ValidationStatus.SUCCESS);
        Assert.assertEquals(result.toURL(), "");
    }

    @Test
    public void shouldFailPasswordMatchesLogin() {
        paramMap.put("password", new String[]{"testPassword"});
        paramMap.put("login", new String[]{"login"});
        User testUser = new User();
        testUser.setPassword(Password.hash("password"));
        when(userService.findUserByLogin("login")).thenReturn(testUser);

        Validator validator = new ValidationBuilder().passwordMatchesLogin(userService).build();
        ValidationResult result = validator.validate(requestMock);

        Assert.assertEquals(result.getValidationStatus(), ValidationStatus.FAILED);
        Assert.assertEquals(result.toURL(), "password=wrong_password");
    }

    @Test
    public void shouldPassPasswordMatchesLogin() {
        paramMap.put("password", new String[]{"password"});
        paramMap.put("login", new String[]{"login"});
        User testUser = new User();
        testUser.setPassword(Password.hash("password"));
        when(userService.findUserByLogin("login")).thenReturn(testUser);

        Validator validator = new ValidationBuilder().passwordMatchesLogin(userService).build();
        ValidationResult result = validator.validate(requestMock);

        Assert.assertEquals(result.getValidationStatus(), ValidationStatus.SUCCESS);
        Assert.assertEquals(requestMock.getAttribute("userInfo"), testUser);
    }

    @Test
    public void shouldCheckPasswordsDifference() {

        paramMap.put("current_password", new String[]{"0000"});
        paramMap.put("repeatedPassword", new String[]{"0001"});

        Validator validator = new ValidationBuilder().checkConfirmingPasswords("current_password").build();
        ValidationResult result = validator.validate(requestMock);

        Assert.assertEquals(result.getValidationStatus(), ValidationStatus.FAILED);
        Assert.assertEquals(result.toURL(),
                "repeatedPassword=passwords_are_different&current_password=passwords_are_different");
    }

    @Test
    public void shouldCheckIfUsersStatus() {
        User testUser = new User();
        testUser.setStatus(UserStatus.BANNED);
        requestMock.setAttribute("userInfo", testUser);

        Validator validator = new ValidationBuilder().userNotActive().build();
        ValidationResult result = validator.validate(requestMock);

        Assert.assertEquals("BANNED=is_banned", result.toURL());

        testUser.setStatus(UserStatus.PENDING);
        requestMock.setAttribute("userInfo", testUser);

        ValidationResult result1 = validator.validate(requestMock);

        Assert.assertEquals("PENDING=registration_not_finished", result1.toURL());
    }
}
