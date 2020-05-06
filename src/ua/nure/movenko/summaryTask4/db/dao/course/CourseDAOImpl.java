package ua.nure.movenko.summaryTask4.db.dao.course;

import org.apache.log4j.Logger;
import ua.nure.movenko.summaryTask4.bean.Journal;
import ua.nure.movenko.summaryTask4.constants.DBFields;
import ua.nure.movenko.summaryTask4.db.dao.*;
import ua.nure.movenko.summaryTask4.entities.*;
import ua.nure.movenko.summaryTask4.enums.CourseStatus;
import ua.nure.movenko.summaryTask4.enums.Role;
import ua.nure.movenko.summaryTask4.exception.OperationDaoException;
import ua.nure.movenko.summaryTask4.util.ViewConfiguration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * CoursDAOImpl based  on implementation of the {@code CourseDAO} interface.
 *
 * @ author M.Movenko
 */
public class CourseDAOImpl extends AbstractOperationDAO<Course> implements CourseDAO {

    private static final Logger LOG = Logger.getLogger(CourseDAOImpl.class);

    private static final String GET_COURSE_BY_ID = "SELECT * FROM courses INNER JOIN themes ON " +
            "courses.theme_id = themes.id INNER JOIN users ON courses.lector_id = users.id  INNER JOIN " +
            "title_dictionary ON courses.title_id = title_dictionary.id WHERE courses.id = ?";

    private static final String GET_COURSE_BY_TITLE = "SELECT * FROM courses INNER JOIN themes ON " +
            "courses.theme_id = themes.id INNER JOIN users ON courses.lector_id = users.id  INNER JOIN " +
            "title_dictionary ON courses.title_id = title_dictionary.id WHERE en = ? OR ru=?";

    private static final String GET_COURSES_BY_LECTOR = "SELECT *, (CASE WHEN  courses.id NOT in " +
            " (SELECT course_id FROM journal WHERE student_id=?) THEN false ELSE true END) AS enrolled FROM " +
            "courses INNER JOIN title_dictionary ON courses.title_id= title_dictionary.id INNER JOIN themes ON " +
            "courses.theme_id=themes.id INNER JOIN users ON courses.lector_id = users.id " +
            "WHERE (users.last_name=?) LIMIT ? OFFSET ?";

    private static final String GET_COURSES_BY_THEME = "SELECT *, (CASE WHEN  courses.id NOT IN (SELECT course_id " +
            "FROM journal WHERE student_id=?) THEN false ELSE true END) AS enrolled FROM courses INNER JOIN " +
            "title_dictionary ON courses.title_id= title_dictionary.id INNER JOIN users" +
            " ON courses.lector_id = users.id INNER JOIN themes ON courses.theme_id=themes.id " +
            "WHERE (en_theme=? or ru_theme=?) LIMIT ? OFFSET ? ";

    private static final String GET_COURSES_BY_LECTOR_AND_THEME = "SELECT *, (CASE WHEN  courses.id NOT IN " +
            "(SELECT course_id FROM journal WHERE student_id=?) THEN false ELSE true END) AS enrolled FROM courses " +
            "INNER JOIN themes ON courses.theme_id=themes.id INNER JOIN title_dictionary ON" +
            " courses.title_id= title_dictionary.id INNER JOIN users ON courses.lector_id = users.id WHERE" +
            " (themes.en_theme=? OR themes.ru_theme=?) AND users.last_name =? LIMIT ? OFFSET ?";

    private static final String GET_ALL_COURSES_ASC_TITLE = "SELECT  * FROM " +
            "courses INNER JOIN title_dictionary ON courses.title_id = title_dictionary.id INNER JOIN themes ON " +
            "courses.theme_id = themes.id INNER JOIN users ON courses.lector_id = users.id ORDER BY title_dictionary.ru ASC";

    private static final String GET_ALL_COURSES_PAGINATED_ORDERED = "SELECT  *, (CASE WHEN  courses.id NOT IN " +
            "(SELECT course_id FROM journal WHERE student_id=?) THEN false ELSE true END) AS enrolled  from courses" +
            " INNER JOIN title_dictionary ON courses.title_id = title_dictionary.id " +
            "INNER JOIN themes ON courses.theme_id = themes.id INNER JOIN users ON " +
            "courses.lector_id = users.id ORDER BY %s %s LIMIT ? OFFSET ?";

    private static final String GET_STUDENT_COURSES = "SELECT * FROM courses INNER JOIN journal " +
            "ON courses.id=journal.course_id INNER JOIN title_dictionary ON" +
            " courses.title_id = title_dictionary.id INNER JOIN themes ON courses.theme_id = themes.id " +
            "INNER JOIN users ON courses.lector_id = users.id WHERE student_id = ?";

    private static final String GET_LECTOR_COURSES = "SELECT * FROM courses INNER JOIN title_dictionary " +
            "ON courses.title_id= title_dictionary.id INNER JOIN themes ON courses.theme_id=themes.id " +
            "INNER JOIN users ON courses.lector_id = users.id WHERE lector_id=? ";

    private static final String DELETE_BY_ID = "DELETE  FROM stdb.courses WHERE id = ?";

    private static final String DELETE_BY_TITLE = "DELETE  FROM title_dictionary WHERE en = ? OR ru=?";

    private static final String ADD_COURSE = "INSERT INTO courses VALUES (DEFAULT, ? ," +
            "(SELECT id FROM themes WHERE en_theme=? or ru_theme=?), (SELECT id FROM users WHERE  " +
            "first_name=? AND last_name=?), ?, ?, ?,?,DEFAULT)";


    private static final String UPDATE_COURSE = "UPDATE courses, title_dictionary SET  title_dictionary.en=?, " +
            "title_dictionary.ru=?, courses.theme_id = (SELECT id FROM themes WHERE en_theme=? or ru_theme=?), courses.lector_id=(SELECT id FROM users WHERE last_name=?), courses.start_date=?," +
            " courses.finish_date=?, courses.duration=?, courses.description=? WHERE   courses.title_id = title_dictionary.id AND courses.id=?";

    private static final String COUNT_COURSES = "SELECT COUNT(id) FROM stdb.courses";

    @Override
    public Course getCourseByTitle(String title, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_COURSE_BY_TITLE)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, title);
            List<Course> records = executeQuery(preparedStatement);
            return records.isEmpty() ? null : records.get(0);
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + GET_COURSE_BY_TITLE + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + GET_COURSE_BY_TITLE + "']", e);
        }
    }

    @Override
    public List<Course> getAll(Connection connection, Integer limit, Integer offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Course> getAll(Connection connection) {
        return getAll(GET_ALL_COURSES_ASC_TITLE, connection);
    }

    @Override
    public List<Course> getAllCoursesOrdered(Connection connection, ViewConfiguration params) {
        String criteriaParameter = Optional.ofNullable(params.getCriteria())
                .filter(parameter -> !params.getCriteria().isEmpty())
                .map(parameter -> getSortCriteria(parameter, params.getLanguage()))
                .orElse("title_dictionary.".concat(params.getLanguage()));
        String orderParameter = Optional.ofNullable(params.getOrder()).orElse("ASC");
        try (PreparedStatement preparedStatement = connection.prepareStatement(String
                .format(GET_ALL_COURSES_PAGINATED_ORDERED, criteriaParameter, orderParameter))) {
            int k = 1;
            preparedStatement.setInt(k++, params.getUserId());
            preparedStatement.setInt(k++, params.getLimit());
            preparedStatement.setInt(k, params.getOffset());
            return executeQuery(preparedStatement);
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + GET_ALL_COURSES_PAGINATED_ORDERED + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + GET_ALL_COURSES_PAGINATED_ORDERED + "']", e);
        }
    }


    @Override
    public List<Course> getCoursesByLectorSurname(ViewConfiguration params, String
            lectorSurname, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_COURSES_BY_LECTOR)) {
            int k = 1;
            preparedStatement.setInt(k++, params.getUserId());
            preparedStatement.setString(k++, lectorSurname);
            preparedStatement.setInt(k++, params.getLimit());
            preparedStatement.setInt(k, params.getOffset());
            return executeQuery(preparedStatement);
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + GET_COURSES_BY_LECTOR + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + GET_COURSES_BY_LECTOR + "']", e);
        }
    }

    @Override
    public List<Course> getCoursesByLectorSurnameAndTheme(ViewConfiguration params, String subject, String lectorSurname, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_COURSES_BY_LECTOR_AND_THEME)) {
            int k = 1;
            preparedStatement.setInt(k++, params.getUserId());
            preparedStatement.setString(k++, subject);
            preparedStatement.setString(k++, subject);
            preparedStatement.setString(k++, lectorSurname);
            preparedStatement.setInt(k++, params.getLimit());
            preparedStatement.setInt(k, params.getOffset());
            return executeQuery(preparedStatement);
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + GET_COURSES_BY_LECTOR_AND_THEME + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + GET_COURSES_BY_LECTOR_AND_THEME + "']", e);
        }
    }

    @Override
    public List<Course> getCoursesByTheme(ViewConfiguration params, String theme, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_COURSES_BY_THEME)) {
            int k = 1;
            preparedStatement.setInt(k++, params.getUserId());
            preparedStatement.setString(k++, theme);
            preparedStatement.setString(k++, theme);
            preparedStatement.setInt(k++, params.getLimit());
            preparedStatement.setInt(k, params.getOffset());
            return executeQuery(preparedStatement);
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + GET_COURSES_BY_THEME + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + GET_COURSES_BY_THEME + "']", e);
        }
    }

    @Override
    public List<Course> getStudentCourses(User student, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_STUDENT_COURSES)) {
            int k = 1;
            preparedStatement.setInt(k, student.getId());
            return executeQuery(preparedStatement, student);
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + GET_STUDENT_COURSES + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + GET_STUDENT_COURSES + "']", e);
        }
    }

    @Override
    public List<Course> getLectorCourses(User lector, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_LECTOR_COURSES)) {
            int k = 1;
            preparedStatement.setInt(k, lector.getId());
            return executeQuery(preparedStatement);
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + GET_LECTOR_COURSES + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + GET_LECTOR_COURSES + "']", e);
        }
    }

    @Override
    public boolean deleteById(int id, Connection connection) {
        return deleteById(id, DELETE_BY_ID, connection);
    }

    @Override
    public boolean deleteByTitle(String title, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_TITLE)) {
            int k = 1;
            preparedStatement.setString(k++, title);
            preparedStatement.setString(k, title);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + DELETE_BY_TITLE + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + DELETE_BY_TITLE + "']", e);
        }
    }


    @Override
    public Course getById(int id, Connection connection) {
        return getById(id, GET_COURSE_BY_ID, connection);
    }

    @Override
    public boolean exists(String title, Connection connection) {
        return getCourseByTitle(title, connection) != null;
    }

    @Override
    public int add(Course course, Connection connection) {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement(ADD_COURSE, Statement.RETURN_GENERATED_KEYS)) {
            int k = 1;
            preparedStatement.setInt(k++, course.getTitleDictionary().getId());
            preparedStatement.setString(k++, course.getTheme().getThemeTitleEn());
            preparedStatement.setString(k++, course.getTheme().getThemeTitleRu());
            preparedStatement.setString(k++, course.getLector().getFirstName());
            preparedStatement.setString(k++, course.getLector().getLastName());
            preparedStatement.setDate(k++, course.getStartDate());
            preparedStatement.setDate(k++, course.getFinishDate());
            preparedStatement.setInt(k++, calculateDuration(course));
            preparedStatement.setString(k, course.getDescription());
            if (preparedStatement.executeUpdate() > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    course.setId(resultSet.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + ADD_COURSE + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + ADD_COURSE + "']", e);
        }
        return course.getId();
    }

    @Override
    public boolean update(Course course, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_COURSE)) {
            int k = 1;
            preparedStatement.setString(k++, course.getTitleDictionary().getEn());
            preparedStatement.setString(k++, course.getTitleDictionary().getRu());
            preparedStatement.setString(k++, course.getTheme().getThemeTitleEn());
            preparedStatement.setString(k++, course.getTheme().getThemeTitleRu());
            preparedStatement.setString(k++, course.getLector().getLastName());
            preparedStatement.setDate(k++, course.getStartDate());
            preparedStatement.setDate(k++, course.getFinishDate());
            preparedStatement.setInt(k++, calculateDuration(course));
            preparedStatement.setString(k++, course.getDescription());
            preparedStatement.setInt(k, course.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + UPDATE_COURSE + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + UPDATE_COURSE + "'] ", e);
        }
    }

    @Override
    public int countCourses(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(COUNT_COURSES)) {
            ResultSet res = preparedStatement.executeQuery();
            int result = 0;
            if (res.next()) {
                result = res.getInt(1);
            }
            return  result;
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + COUNT_COURSES + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + COUNT_COURSES + "']", e);
        }
    }

    @Override
    protected Course extract(ResultSet resultSet) throws SQLException {
        Course course = new Course();
        course.setId(resultSet.getInt(DBFields.ENTITY_ID));
        course.setTitleDictionary(extractTitleDictionary(resultSet));
        course.setTheme(extractTheme(resultSet));
        course.setLector(extractLector(resultSet));
        course.setStartDate(resultSet.getDate(DBFields.START_DATE));
        course.setFinishDate(resultSet.getDate(DBFields.FINISH_DATE));
        course.setDuration(resultSet.getInt(DBFields.DURATION));
        course.setDescription(resultSet.getString(DBFields.DESCRIPTION));
        course.setStudentsEnrolled(resultSet.getInt(DBFields.NUMBER_OF_STUDENTS));
        course.setStatus(defineCourseStatus(course));
        try {
            course.setUserEnrolled(resultSet.getBoolean(DBFields.ENROLLED));
        } catch (SQLException e) {
            course.setUserEnrolled(false);
        }
        return course;
    }

    private int calculateDuration(Course course) {
        long millisecInDay = 86400000;
        Date finishDate = course.getFinishDate();
        Date startdate = course.getStartDate();
        return (int) ((finishDate.getTime() - startdate.getTime()) / millisecInDay);
    }

    private CourseStatus defineCourseStatus(Course course) {
        java.util.Date currentDate = new java.util.Date();
        if (course.getStartDate().compareTo(currentDate) > 0) {
            return CourseStatus.PENDING;
        } else if (course.getStartDate().compareTo(currentDate) <= 0 && course.
                getFinishDate().compareTo(currentDate) > 0) {
            return CourseStatus.IN_PROGRESS;
        } else {
            return CourseStatus.FINISHED;
        }
    }

    private User extractLector(ResultSet resultSet) throws SQLException {
        User lector = new User();
        lector.setId(resultSet.getInt(DBFields.LECTOR_ID));
        lector.setLogin(resultSet.getString(DBFields.USER_LOGIN));
        lector.setPassword(resultSet.getString(DBFields.USER_PASSWORD));
        lector.setEmail(resultSet.getString(DBFields.USER_EMAIL));
        lector.setFirstName(resultSet.getString(DBFields.USER_FIRST_NAME));
        lector.setLastName(resultSet.getString(DBFields.USER_LAST_NAME));
        lector.setRole(Role.getRole(resultSet.getInt(DBFields.USER_ROLE_ID)));
        return lector;
    }

    private Theme extractTheme(ResultSet resultSet) throws SQLException {
        Theme theme = new Theme();
        theme.setId(resultSet.getInt(DBFields.THEME_ID));
        theme.setThemeTitleEn(resultSet.getString(DBFields.THEME_EN));
        theme.setThemeTitleRu(resultSet.getString(DBFields.THEME_RU));
        return theme;
    }

    private TitleDictionary extractTitleDictionary(ResultSet resultSet) throws SQLException {
        TitleDictionary title = new TitleDictionary();
        title.setId(resultSet.getInt(DBFields.TITLE_ID));
        title.setEn(resultSet.getString(DBFields.TITLE_EN));
        title.setRu(resultSet.getString(DBFields.TITLE_RU));
        return title;
    }

    private String getSortCriteria(String criteria, String language) {
        if ("duration".equals(criteria)) {
            return "duration";
        } else if ("students_count".equals(criteria)) {
            return "students_enrolled";
        } else {
            return criteria.concat(".").concat(language);
        }
    }

    private List<Course> executeQuery(PreparedStatement preparedStatement, User student) throws SQLException {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Course> results = new ArrayList<>();
            while (resultSet.next()) {
                Course record = extract(resultSet);
                Journal journalRaw = new Journal();
                journalRaw.addRaw(student, resultSet.getString(DBFields.MARK));
                record.setJournal(journalRaw);
                results.add(record);
            }
            return results;
        }
    }
}


