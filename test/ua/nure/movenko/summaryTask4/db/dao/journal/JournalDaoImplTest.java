package ua.nure.movenko.summaryTask4.db.dao.journal;

import com.mysql.jdbc.Driver;
import org.junit.*;
import ua.nure.movenko.summaryTask4.bean.Journal;
import ua.nure.movenko.summaryTask4.db.dao.course.CourseDAO;
import ua.nure.movenko.summaryTask4.db.dao.course.CourseDAOImpl;
import ua.nure.movenko.summaryTask4.db.dao.dictionary.TitleDictionaryDAOImpl;
import ua.nure.movenko.summaryTask4.db.dao.dictionary.TitleDictionaryDao;
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

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class JournalDaoImplTest {
    private static final CourseDAO courseDAO = new CourseDAOImpl();
    private static final ThemeDAO themeDAO = new ThemeDAOImpl();
    private static final UserDAO userDAO = new UserDAOImpl();
    private static final TitleDictionaryDao dictionaryDao = new TitleDictionaryDAOImpl();
    private static Connection connection;
    private static int titleId;
    private static int lectorId;
    private static int courseId;
    private final JournalDAO journalDao = new JournalDAOImpl();


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
            Theme theme = new Theme("Maths", "Математика");
            themeDAO.add(theme, connection);
            titleId = dictionaryDao.add(new TitleDictionary("Title", "Название"), connection);
            lectorId = userDAO.add(createLector(), connection);
            courseId= courseDAO.add(prepareCourse(theme), connection);
            connection.close();
        } catch (SQLException e) {
            System.out.println("Can't close connection");
            e.printStackTrace();
        }
    }


    @Before
    public void setUp() {
        connection = getConnection();
    }

    @Test
    public void testEnrollStudent() {
        User student = createStudent();
        userDAO.add(student, connection);
        Course beforeTest = courseDAO.getCourseByTitle("Title", connection);

        journalDao.enrollStudent(student, connection, "Title");
        Course afterTest = courseDAO.getCourseByTitle("Title", connection);
        Assert.assertEquals(1, afterTest.getStudentsEnrolled());
        Assert.assertNotEquals(beforeTest.getStudentsEnrolled(), afterTest.getStudentsEnrolled());
    }


    @Test
    public void shouldGetCourseJournal() {
        User student1 = createStudent();
        User student2 = createStudent();
        student2.setLogin("student2");
        student2.setEmail("student2email@i.ua");
        userDAO.add(student1, connection);
        userDAO.add(student2, connection);
        User lector = userDAO.getById(lectorId, connection);
        journalDao.enrollStudent(student1, connection, "Title");
        journalDao.enrollStudent(student2, connection, "Title");

        Journal result = journalDao.getCourseJournal(connection, lector, courseId);
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.getStudentsMarks().size());
    }

    @Test
    public void shouldUpdateJournal(){
        User student1 = createStudent();
        User student2 = createStudent();
        student2.setLogin("student2");
        student2.setEmail("student2email@i.ua");
        userDAO.add(student1, connection);
        userDAO.add(student2, connection);
        User lector = userDAO.getById(lectorId, connection);
        journalDao.enrollStudent(student1, connection, "Title");
        journalDao.enrollStudent(student2, connection, "Title");
        Journal beforeUpdate = journalDao.getCourseJournal(connection, lector, courseId);

        journalDao.updateJournal(List.of(student2.getId()), List.of("b"), courseId, connection);
        Journal afterUpdate = journalDao.getCourseJournal(connection, lector, courseId);
        Assert.assertNotEquals(beforeUpdate.getStudentsMarks(), afterUpdate.getStudentsMarks());
    }

    @After
    public void tearDown() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE  FROM journal");
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("DELETE  FROM users WHERE role_id=2");
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("UPDATE courses SET students_enrolled = 0 ");
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            System.err.println("Can't close connection.\n");
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDownClass() {
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE  FROM title_dictionary");
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("DELETE  FROM themes");
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("DELETE FROM users");
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Course prepareCourse(Theme theme) {
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
}
