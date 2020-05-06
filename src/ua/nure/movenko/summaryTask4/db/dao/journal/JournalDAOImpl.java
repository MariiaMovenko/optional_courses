package ua.nure.movenko.summaryTask4.db.dao.journal;

import org.apache.log4j.Logger;
import ua.nure.movenko.summaryTask4.constants.DBFields;
import ua.nure.movenko.summaryTask4.bean.Journal;
import ua.nure.movenko.summaryTask4.entities.Course;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.CourseStatus;
import ua.nure.movenko.summaryTask4.exception.OperationDaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * JournalDAOImpl based  on implementation of the {@code JournalDAO} interface.
 *
 * @ author M.Movenko
 */
public class JournalDAOImpl implements JournalDAO {

    private static final Logger LOG = Logger.getLogger(JournalDAOImpl.class);

    private static final String ENROLL_STUDENT = "INSERT INTO journal (student_id, course_id) " +
            "select users.id, courses.id FROM users, courses INNER JOIN title_dictionary " +
            "ON courses.title_id= title_dictionary.id WHERE  users.id=? AND (title_dictionary.en =? " +
            "OR title_dictionary.ru =?)";

    private static final String GET_COURSE_JOURNAL = "SELECT course_id, start_date, finish_date, student_id, first_name, last_name, email, mark " +
            "FROM journal INNER JOIN courses ON journal.course_id= courses.id " +
            "INNER JOIN users ON journal.student_id = users.id WHERE lector_id = ? AND " +
            "course_id = ?  ORDER BY last_name ASC";

    private static final String INCREASE_STUDENTS_AT_COURSE_COUNT = "UPDATE courses SET " +
            "students_enrolled = (students_enrolled + 1) WHERE  title_id = " +
            "(SELECT id FROM  title_dictionary WHERE en=?  OR ru=?)";

    private static final String UPDATE_JOURNAL = "UPDATE journal SET mark=? where student_id=? " +
            "AND course_id = ? ";

    @Override
    public boolean enrollStudent(User student, Connection connection, String... courseTitles) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(ENROLL_STUDENT)) {
            preparedStatement.setInt(1, student.getId());
            for (String courseTitle : courseTitles) {
                preparedStatement.setString(2, courseTitle);
                preparedStatement.setString(3, courseTitle);
                preparedStatement.executeUpdate();
                increaseStudentsCourseCount(courseTitle, connection);
            }
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + ENROLL_STUDENT + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + ENROLL_STUDENT + "']", e);
        }
        return true;
    }

    @Override
    public Journal getCourseJournal(Connection connection, User lector, Integer courseId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_COURSE_JOURNAL)) {
            int k = 1;
            preparedStatement.setInt(k++, lector.getId());
            preparedStatement.setInt(k, courseId);
            return executeQuery(preparedStatement);
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + GET_COURSE_JOURNAL + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + GET_COURSE_JOURNAL + "']", e);
        }
    }

    @Override
    public boolean updateJournal(List<Integer> studentId, List<String> mark, Integer courseId, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_JOURNAL)) {
            for (int i = 0; i < studentId.size() && i < mark.size(); i++) {
                int k = 1;
                preparedStatement.setString(k++, mark.get(i));
                preparedStatement.setInt(k++, studentId.get(i));
                preparedStatement.setInt(k, courseId);
                preparedStatement.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + UPDATE_JOURNAL + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + UPDATE_JOURNAL + "'] ", e);
        }
    }

    private Journal executeQuery(PreparedStatement preparedStatement) throws SQLException {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            Journal journal = new Journal();
            Course course = new Course();
            Map<User, String> results = new LinkedHashMap<>();
            while (resultSet.next()) {
                course = extractCourseData(resultSet);
                User student = extractStudent(resultSet);
                String mark = Optional.ofNullable(resultSet.getString(DBFields.MARK)).orElse("Not defined");
                results.put(student, mark);
            }
            journal.setStudentsMarks(results);
            journal.setCourseInfo(course);
            return journal;
        }
    }

    private User extractStudent(ResultSet resultSet) throws SQLException {
        User student = new User();
        student.setId(resultSet.getInt(DBFields.STUDENT_ID));
        student.setFirstName(resultSet.getString(DBFields.USER_FIRST_NAME));
        student.setLastName(resultSet.getString(DBFields.USER_LAST_NAME));
        student.setEmail(resultSet.getString(DBFields.USER_EMAIL));
        return student;
    }

    private Course extractCourseData(ResultSet resultSet) throws SQLException {
        Course course = new Course();
        course.setId(resultSet.getInt(DBFields.COURSE_ID));
        course.setStartDate(resultSet.getDate(DBFields.START_DATE));
        course.setFinishDate(resultSet.getDate(DBFields.FINISH_DATE));
        course.setStatus(defineCourseStatus(course));
        return course;
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

    private void increaseStudentsCourseCount(String courseTitle, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INCREASE_STUDENTS_AT_COURSE_COUNT);
        int k = 1;
        preparedStatement.setString(k++, courseTitle);
        preparedStatement.setString(k, courseTitle);
        preparedStatement.executeUpdate();
    }

}

