package ua.nure.movenko.summaryTask4.db.dao.course;

import com.mysql.jdbc.Driver;
import org.junit.*;
import ua.nure.movenko.summaryTask4.db.dao.dictionary.TitleDictionaryDAOImpl;
import ua.nure.movenko.summaryTask4.db.dao.dictionary.TitleDictionaryDao;
import ua.nure.movenko.summaryTask4.db.dao.journal.JournalDAO;
import ua.nure.movenko.summaryTask4.db.dao.journal.JournalDAOImpl;
import ua.nure.movenko.summaryTask4.db.dao.theme.ThemeDAO;
import ua.nure.movenko.summaryTask4.db.dao.theme.ThemeDAOImpl;
import ua.nure.movenko.summaryTask4.db.dao.user.UserDAO;
import ua.nure.movenko.summaryTask4.db.dao.user.UserDAOImpl;
import ua.nure.movenko.summaryTask4.entities.Course;
import ua.nure.movenko.summaryTask4.entities.Theme;
import ua.nure.movenko.summaryTask4.entities.TitleDictionary;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.Role;
import ua.nure.movenko.summaryTask4.enums.UserStatus;
import ua.nure.movenko.summaryTask4.util.ViewConfiguration;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class CourseDaoImplTest {
    private final CourseDAO courseDAO = new CourseDAOImpl();
    private static final ThemeDAO themeDAO = new ThemeDAOImpl();
    private static final UserDAO userDAO = new UserDAOImpl();
    private static final JournalDAO journalDao = new JournalDAOImpl();
    private static final TitleDictionaryDao dictionaryDao = new TitleDictionaryDAOImpl();
    private static int titleId;
    private static int lectorId;
    private static Connection connection;


    public static Connection getConnection() {
        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "admin");

        Connection connection = null;
        try {
            connection = ((Driver) Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance())
                    .connect("jdbc:mysql://localhost:3306/test_db?serverTimezone=UTC", props);
        } catch (ClassNotFoundException e) {
            System.err.println("Where is your JDBC Driver?\n");
            e.printStackTrace();
            System.exit(1);
        } catch (SQLException e) {
            System.err.println("Connection Failed.\n");
            e.printStackTrace();
            System.exit(1);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return connection;
    }

    @BeforeClass
    public static void prepareTablesForTesting() {
        try {
            connection = getConnection();
            themeDAO.add(new Theme("Maths", "Математика"), connection);
            themeDAO.add(new Theme("Biology", "Биология"), connection);
            lectorId = userDAO.add(createLector(), connection);
            connection.close();
        } catch (SQLException e) {
            System.out.println("Can't close connection");
            e.printStackTrace();
        }
    }


    @Before
    public void setUp(){
        connection = getConnection();
        titleId = dictionaryDao.add(new TitleDictionary("Title", "Название"), connection);
    }

    @After
    public void tearDown() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE  FROM title_dictionary");
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            System.err.println("Can't close connection.\n");
            e.printStackTrace();
        }
    }

    @Test
    public void testAddCourse() {
        Course testCourse = prepareCourse(new Theme("Maths", "Математика"));

        int courseId = courseDAO.add(testCourse, connection);
        Assert.assertTrue(courseId > 0);

        Course addedCourse = courseDAO.getById(courseId, connection);
        Assert.assertNotNull(addedCourse);
        Assert.assertEquals(testCourse.getTitleDictionary().getRu(), addedCourse.getTitleDictionary().getRu());
        Assert.assertTrue(addedCourse.getDuration() > 0);
    }

    @Test
    public void testGetCourseByTitle() {
        Course testCourse = prepareCourse(new Theme("Maths", "Maths"));
        courseDAO.add(testCourse, connection);

        Course result = courseDAO.getCourseByTitle("Title", connection);
        Assert.assertNotNull(result);
        Assert.assertEquals(testCourse.getId(), result.getId());
    }

    @Test
    public void testGetAll() {
        Course testCourse1 = prepareCourse(new Theme("Maths", "Maths"));
        Course testCourse2 = prepareCourse(new Theme("Biology", "Биология"));
        TitleDictionary titleDictionary2 = new TitleDictionary("Title2", "Название2");
        dictionaryDao.add(titleDictionary2, connection);
        testCourse2.setTitleDictionary(titleDictionary2);
        courseDAO.add(testCourse1, connection);
        courseDAO.add(testCourse2, connection);

        List<Course> result = courseDAO.getAll(connection);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(testCourse1.getId(), result.get(0).getId());
        Assert.assertEquals(testCourse2.getId(), result.get(1).getId());
    }

    @Test
    public void testGetAllOrdered() {
        ViewConfiguration params = createParam();
        params.setCriteria("duration");
        Course testCourse1 = prepareCourse(new Theme("Maths", "Maths"));
        Course testCourse2 = prepareCourse(new Theme("Biology", "Биология"));
        TitleDictionary titleDictionary2 = new TitleDictionary("Title2", "Название2");
        dictionaryDao.add(titleDictionary2, connection);
        testCourse2.setTitleDictionary(titleDictionary2);
        courseDAO.add(testCourse1, connection);
        courseDAO.add(testCourse2, connection);

        List<Course> result = courseDAO.getAllCoursesOrdered(connection, params);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(testCourse1.getId(), result.get(0).getId());
    }

    @Test
    public void testGetCoursesByLectorSurname() {
        ViewConfiguration params = createParam();
        params.setLimit(100);
        Course testCourse1 = prepareCourse(new Theme("Maths", "Maths"));
        Course testCourse2 = prepareCourse(new Theme("Biology", "Биология"));
        TitleDictionary titleDictionary2 = new TitleDictionary("Title2", "Название2");
        dictionaryDao.add(titleDictionary2, connection);
        testCourse2.setTitleDictionary(titleDictionary2);
        courseDAO.add(testCourse1, connection);
        courseDAO.add(testCourse2, connection);

        List<Course> result = courseDAO.getCoursesByLectorSurname(params, "LN", connection);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("LN", result.get(0).getLector().getLastName());
        Assert.assertEquals("LN", result.get(1).getLector().getLastName());
        Assert.assertEquals("Biology", result.get(1).getTheme().getThemeTitleEn());
        Assert.assertEquals("Maths", result.get(0).getTheme().getThemeTitleEn());
    }

    @Test
    public void testGetCoursesByLectorAndTheme() {
        ViewConfiguration params = createParam();
        Course testCourse1 = prepareCourse(new Theme("Maths", "Maths"));
        Course testCourse2 = prepareCourse(new Theme("Biology", "Биология"));
        TitleDictionary titleDictionary2 = new TitleDictionary("Title2", "Название2");
        dictionaryDao.add(titleDictionary2, connection);
        testCourse2.setTitleDictionary(titleDictionary2);
        courseDAO.add(testCourse1, connection);
        courseDAO.add(testCourse2, connection);

        List<Course> result = courseDAO.getCoursesByLectorSurnameAndTheme(params, "Biology", "LN", connection);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("Biology", result.get(0).getTheme().getThemeTitleEn());
        Assert.assertEquals("Title2", result.get(0).getTitleDictionary().getEn());
    }

    @Test
    public void testGetCoursesByTheme() {
        ViewConfiguration params = createParam();
        Course testCourse1 = prepareCourse(new Theme("Maths", "Maths"));
        Course testCourse2 = prepareCourse(new Theme("Biology", "Биология"));
        TitleDictionary titleDictionary2 = new TitleDictionary("Title2", "Название2");
        dictionaryDao.add(titleDictionary2, connection);
        testCourse2.setTitleDictionary(titleDictionary2);
        courseDAO.add(testCourse1, connection);
        courseDAO.add(testCourse2, connection);

        List<Course> result = courseDAO.getCoursesByTheme(params, "Programming", connection);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testDeleteByTitle() {
        Course testCourse = prepareCourse(new Theme("Biology", "Biology"));
        courseDAO.add(testCourse, connection);

        boolean result = courseDAO.deleteByTitle(testCourse.getTitleDictionary().getRu(), connection);
        int coursesAfterDeleting = courseDAO.getAll(connection).size();
        Assert.assertTrue(result);
        Assert.assertEquals(0, coursesAfterDeleting);
    }

    @Test
    public void testUpdateCourse() {
        Course oldCourse = prepareCourse(new Theme("Biology", "Biology"));
        System.out.println("Before updating: " + oldCourse);
        courseDAO.add(oldCourse, connection);

        oldCourse.getTitleDictionary().setRu("Новое название");
        oldCourse.getTitleDictionary().setEn("New Title");

        boolean result = courseDAO.update(oldCourse, connection);
        Course afterUpdate = courseDAO.getById(oldCourse.getId(), connection);
        Assert.assertTrue(result);
        Assert.assertEquals(oldCourse.getId(), afterUpdate.getId());
        Assert.assertEquals("New Title", afterUpdate.getTitleDictionary().getEn());
    }

    @Test
    public void testGetStudentCourses() {
        User student = createStudent();
        userDAO.add(student, connection);
        Course testCourse1 = prepareCourse(new Theme("Maths", "Maths"));
        Course testCourse2 = prepareCourse(new Theme("Biology", "Биология"));
        TitleDictionary titleDictionary2 = new TitleDictionary("Title2", "Название2");
        dictionaryDao.add(titleDictionary2, connection);
        testCourse2.setTitleDictionary(titleDictionary2);
        courseDAO.add(testCourse1, connection);
        courseDAO.add(testCourse2, connection);
        journalDao.enrollStudent(student, connection, "Title");
        journalDao.enrollStudent(student, connection, "Title2");

        List<Course> result = courseDAO.getStudentCourses(student, connection);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(testCourse1.getId(), result.get(0).getId());
        Assert.assertEquals(testCourse2.getId(), result.get(1).getId());
    }

    @Test
    public void testGetLectorCourses() {
        Course testCourse1 = prepareCourse(new Theme("Maths", "Maths"));
        courseDAO.add(testCourse1, connection);
        User lector = createLector();

        List<Course> result = courseDAO.getLectorCourses(lector, connection);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(lector.getId(), result.get(0).getLector().getId());
    }


    private Course prepareCourse(Theme theme) {
        Course course = new Course();
        course.setTitleDictionary(new TitleDictionary("Title", "Название"));
        course.getTitleDictionary().setId(titleId);
        course.setTheme(theme);
        User lector = new User();
        lector.setFirstName("FN");
        lector.setLastName("LN");
        course.setLector(lector);
        course.setStartDate(Date.valueOf("2020-10-10"));
        course.setFinishDate(Date.valueOf("2020-12-10"));
        course.setDescription("Some description");
        return course;
    }


    private static User createLector() {
        User testUser = new User();
        testUser.setId(lectorId);
        testUser.setLogin("login");
        testUser.setPassword("pass");
        testUser.setFirstName("FN");
        testUser.setLastName("LN");
        testUser.setEmail("email@i.ua");
        testUser.setRole(Role.LECTOR);
        testUser.setStatus(UserStatus.ACTIVE);
        return testUser;
    }

    private static User createStudent() {
        User testUser = new User();
        testUser.setLogin("studentLogin");
        testUser.setPassword("pass");
        testUser.setFirstName("studentName");
        testUser.setLastName("studentSurname");
        testUser.setEmail("student@email.ua");
        testUser.setRole(Role.STUDENT);
        testUser.setStatus(UserStatus.ACTIVE);
        return testUser;
    }

    private ViewConfiguration createParam() {
        ViewConfiguration params = new ViewConfiguration();
        params.setLanguage("en");
        params.setLimit(1);
        params.setOffset(0);
        params.setUserId(-1);
        params.setCriteria("title_dictionary");
        return params;
    }

    @AfterClass
    public static void tearDownClass() {
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE  FROM themes");
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("DELETE  FROM title_dictionary");
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("DELETE FROM users");
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
