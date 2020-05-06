package ua.nure.movenko.summaryTask4.services.course;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.nure.movenko.summaryTask4.db.dao.course.CourseDAO;
import ua.nure.movenko.summaryTask4.db.dao.dictionary.TitleDictionaryDao;
import ua.nure.movenko.summaryTask4.db.transaction.TransactionManagerMock;
import ua.nure.movenko.summaryTask4.entities.Course;
import ua.nure.movenko.summaryTask4.entities.Theme;
import ua.nure.movenko.summaryTask4.entities.TitleDictionary;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.CourseStatus;
import ua.nure.movenko.summaryTask4.enums.Role;
import ua.nure.movenko.summaryTask4.enums.UserStatus;
import ua.nure.movenko.summaryTask4.models.CourseModel;
import ua.nure.movenko.summaryTask4.models.StudentCourseModel;
import ua.nure.movenko.summaryTask4.util.ViewConfiguration;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CourseServiceImplTest {

    @Mock
    private CourseDAO courseDAO;

    @Mock
    private TitleDictionaryDao titleDictionaryDao;

    private TransactionManagerMock transactionManager = new TransactionManagerMock();

    @InjectMocks
    private CourseService courseService = new CourseServiceImpl(transactionManager, courseDAO, titleDictionaryDao);

    @Test
    public void shouldGetCoursesByLectorSurname() throws Exception {
        Course course = createCourse(CourseStatus.IN_PROGRESS, false);
        ViewConfiguration param = new ViewConfiguration();
        param.setLanguage("en");
        List<Course> courses = List.of(course);
        when(courseDAO.getCoursesByLectorSurname(param, "Petrov",
                transactionManager.getConnectionForTest())).thenReturn(courses);

        List<CourseModel> result = courseService.getCoursesByLectorSurname(param, "Petrov");

        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).getId(), 1);
        Assert.assertEquals(result.get(0).getEndDate(), "2020-04-12");
        Assert.assertEquals(result.get(0).getId(), 1);
        Assert.assertEquals(result.get(0).getStudentsEnrolled(), 5);
        Assert.assertEquals(result.get(0).getRuTitle(), "TitleRu");
        Assert.assertEquals(result.get(0).getDuration(), 366);

        transactionManager.verifyExecutedWithoutTransaction();
    }

    @Test
    public void shouldGetCoursesByThemeAndLector() throws Exception {
        Course course = createCourse(CourseStatus.FINISHED, false);
        ViewConfiguration param = new ViewConfiguration();
        param.setLanguage("en");
        List<Course> courses = List.of(course);
        when(courseDAO.getCoursesByLectorSurnameAndTheme(param, "ThemeRu", "Petrov",
                transactionManager.getConnectionForTest())).thenReturn(courses);

        List<CourseModel> result = courseService.getCoursesByThemeAndLector(param, "ThemeRu", "Petrov");

        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).getId(), 1);
        Assert.assertEquals(result.get(0).getEndDate(), "2020-04-12");
        Assert.assertEquals(result.get(0).getStudentsEnrolled(), 5);
        Assert.assertEquals(result.get(0).getRuTitle(), "TitleRu");
        Assert.assertEquals(result.get(0).getLector(), "Ivan Petrov");
        Assert.assertEquals(result.get(0).getDuration(), 366);

        transactionManager.verifyExecutedWithoutTransaction();
    }

    @Test
    public void shouldGetCoursesByTheme() throws Exception {
        Course course = createCourse(CourseStatus.PENDING, false);
        ViewConfiguration param = new ViewConfiguration();
        param.setLanguage("en");
        List<Course> courses = List.of(course);
        when(courseDAO.getCoursesByTheme(param, "ThemeRu",
                transactionManager.getConnectionForTest())).thenReturn(courses);

        List<CourseModel> result = courseService.getCoursesByTheme(param, "ThemeRu");

        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).getId(), 1);
        Assert.assertEquals(result.get(0).getTheme(), "ThemeEn");
        Assert.assertEquals(result.get(0).getEndDate(), "2020-04-12");
        Assert.assertEquals(result.get(0).getStudentsEnrolled(), 5);
        Assert.assertEquals(result.get(0).getEnTitle(), "TitleEn");
        Assert.assertEquals(result.get(0).getLector(), "Ivan Petrov");
        Assert.assertEquals(result.get(0).getDuration(), 366);

        transactionManager.verifyExecutedWithoutTransaction();
    }

    @Test
    public void shouldGetAllCourses() throws Exception {
        String language = "ru";
        Course course1 = createCourse(CourseStatus.FINISHED, false);
        Course course2 = createCourse(CourseStatus.IN_PROGRESS, true);
        course2.setId(2);
        List<Course> courses = List.of(course1, course2);
        when(courseDAO.getAll(transactionManager.getConnectionForTest())).thenReturn(courses);

        List<CourseModel> result = courseService.getAllCourses(language);

        Assert.assertEquals(result.size(), 2);
        Assert.assertEquals(result.get(0).getId(), 1);
        Assert.assertEquals(result.get(1).getId(), 2);
        Assert.assertEquals(result.get(0).getStatus(), CourseStatus.FINISHED);
        Assert.assertEquals(result.get(1).getStatus(), CourseStatus.IN_PROGRESS);
        Assert.assertEquals(result.get(0).getStudentsEnrolled(), 5);
        Assert.assertEquals(result.get(0).getTitle(), "TitleRu");
        Assert.assertEquals(result.get(0).getLector(), "Ivan Petrov");
        Assert.assertEquals(result.get(0).getDuration(), 366);

        transactionManager.verifyExecutedWithoutTransaction();
    }

    @Test
    public void getStudentCourses() throws Exception {
        User student = createUser(Role.STUDENT, UserStatus.BANNED);
        Course course2 = createCourse(CourseStatus.IN_PROGRESS, true);
        course2.setId(2);
        List<Course> courses = List.of(course2);
        when(courseDAO.getStudentCourses(student, transactionManager.getConnectionForTest())).thenReturn(courses);

        List<StudentCourseModel> result = courseService.getStudentCourses(student, CourseStatus.IN_PROGRESS, "ru");

        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).getId(), 2);
        Assert.assertEquals(result.get(0).getStatus(), CourseStatus.IN_PROGRESS);
        Assert.assertEquals(result.get(0).getStudentsEnrolled(), 5);
        Assert.assertEquals(result.get(0).getTitle(), "TitleRu");
        Assert.assertEquals(result.get(0).getLector(), "Ivan Petrov");
        Assert.assertEquals(result.get(0).getDuration(), 366);
        Assert.assertTrue(result.get(0).isEnrolled());
        transactionManager.verifyExecutedWithoutTransaction();
    }

    @Test
    public void shouldGetLectorCourses() throws Exception {
        User lector = createUser(Role.LECTOR, UserStatus.ACTIVE);
        Course course = createCourse(CourseStatus.IN_PROGRESS, false);
        course.setId(2);
        List<Course> courses = List.of(course);
        when(courseDAO.getLectorCourses(lector, transactionManager.getConnectionForTest())).thenReturn(courses);

        List<CourseModel> result = courseService.getLectorCourses(lector, CourseStatus.IN_PROGRESS, "ru");

        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).getId(), 2);
        Assert.assertEquals(result.get(0).getStatus(), CourseStatus.IN_PROGRESS);
        Assert.assertEquals(result.get(0).getStudentsEnrolled(), 5);
        Assert.assertEquals(result.get(0).getTitle(), "TitleRu");
        Assert.assertEquals(result.get(0).getLector(), "Ivan Petrov");
        Assert.assertEquals(result.get(0).getDuration(), 366);
        Assert.assertFalse(result.get(0).isEnrolled());
        transactionManager.verifyExecutedWithoutTransaction();
    }

    @Test
    public void shouldAddCourse() throws Exception {
        Course courseToAdd = createCourse(CourseStatus.PENDING, false);
        when(courseDAO.add(courseToAdd, transactionManager.getConnectionForTest())).thenReturn(25);

        courseService.addCourse(courseToAdd);

        transactionManager.verifyExecutedInTransaction(Connection.TRANSACTION_REPEATABLE_READ);
    }

    @Test
    public void shouldDeleteCourse() throws Exception {
        when(courseDAO.deleteByTitle("TestTitle", transactionManager.getConnectionForTest())).thenReturn(false);

        courseService.deleteCourse("TestTitle");

        transactionManager.verifyExecutedWithoutTransaction();
    }

    @Test
    public void shouldUpdateCourse() throws Exception {
        Course updatedCourse = createCourse(CourseStatus.IN_PROGRESS, false);
        when(courseDAO.update(updatedCourse, transactionManager.getConnectionForTest())).thenReturn(false);

        courseService.updateCourse(updatedCourse);

        transactionManager.verifyExecutedInTransaction(Connection.TRANSACTION_REPEATABLE_READ);
    }

    private Course createCourse(CourseStatus courseStatus, boolean userEnrolled) {
        Course course = new Course();
        course.setId(1);
        course.setTitleDictionary(new TitleDictionary("TitleEn", "TitleRu"));
        course.setTheme(new Theme("ThemeEn", "ThemeRu"));
        User lector = createUser(Role.LECTOR, UserStatus.ACTIVE);
        course.setLector(lector);
        course.setStartDate(Date.valueOf("2020-04-11"));
        course.setFinishDate(Date.valueOf("2020-04-12"));
        course.setStudentsEnrolled(5);
        course.setUserEnrolled(userEnrolled);
        course.setDuration(366);
        course.setStatus(courseStatus);
        return course;
    }

    private User createUser(Role role, UserStatus status) {
        User user = new User();
        user.setLogin("login");
        user.setPassword("password");
        user.setId(1);
        user.setRole(role);
        user.setLastName("Petrov");
        user.setFirstName("Ivan");
        user.setEmail("test@gmail.com");
        user.setStatus(status);
        return user;
    }
}
