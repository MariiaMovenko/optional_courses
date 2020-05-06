package ua.nure.movenko.summaryTask4.db.dao.course;

import ua.nure.movenko.summaryTask4.db.dao.OperationDAO;
import ua.nure.movenko.summaryTask4.entities.Course;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.util.ViewConfiguration;

import java.sql.Connection;
import java.util.List;

public interface CourseDAO extends OperationDAO<Course> {

    /**
     * Returns  course with the specified title.
     *
     * @param title is title of course to return
     * @param connection to Database
     * @return course with the specified title.
     */
    Course getCourseByTitle(String title, Connection connection);

    /**
     * Returns list of courses of the specified Lecturer and on the specified subject.
     * @param params represents an util class object which contains information concerning view layer
     * such as chosen locale, limit courses on the page, page to show
     * @param lectorSurname is criteria for selecting courses to return
     * @param connection to Database
     * @return list of courses of specified  lecturer
     */
    List<Course> getCoursesByLectorSurname(ViewConfiguration params,
                                           String lectorSurname, Connection connection);

    /**
     * Returns courses of a particular lecturer and   on the specified subject.
     * @param params represents an util class object which contains information concerning view layer
     *               such as chosen locale, limit courses on the page, page to show
     * @param lectorSurname is criteria for choosing courses to return
     * @param connection    to Database
     * @return list of courses of a particular lecturer and on the specified subject
     */
    List<Course> getCoursesByLectorSurnameAndTheme(ViewConfiguration params, String subject,
                                                   String lectorSurname, Connection connection);

    /**
     * Returns list of courses on the specified subject.
     *
     * @param params represents an util class object which contains information concerning view layer such
     *               as chosen locale, limit courses on the page, page to show
     * @param subject    is criteria for choosing courses to return
     * @param connection to Database
     * @return Returns list of courses on the specified subject.
     */
    List<Course> getCoursesByTheme(ViewConfiguration params, String subject, Connection connection);

    /**
     * Finds and returns all courses from database in the specific order defined in the {@code params}
     *
     * @param connection to Database
     * @param params     represents an util class object which contains information concerning view layer such as chosen locale, limit courses on the page,
     *                   page to show, courses order to show and   order criteria
     * @return Returns list of courses sorted in the specific  order which defined in  {@code params}
     */
    List<Course> getAllCoursesOrdered(Connection connection, ViewConfiguration params);

    /**
     * Returns list of courses particular {@code student.getUserId()} enrolled to
     *
     * @param student is a user by whose id courses to return should be found
     * @param connection to Database
     * @return list of courses particular student enrolled to
     */
    List<Course> getStudentCourses(User student, Connection connection);

    /**
     * Returns list of courses  that particular {@code lector.getUserId()} teaches
     *
     * @param lector     is a user by whose id courses to return should be found
     * @param connection to Database
     * @return list of courses particular {@code lector.getUserId()} teaches
     */
    List<Course> getLectorCourses(User lector, Connection connection);

    /**
     * Check if  the course with the particular {@param title} exists in the database
     *
     * @param title of chosen in the database course
     * @param connection to Database
     * @return {@code true} if this database contained the course with the specified title
     */
    boolean exists(String title, Connection connection);

    /**
     * Removes from the database course with particular {@param title} if it is present
     * @param title of the course which should be deleted
     * @param connection to Database
     * @return {@code true} if this database contained the course with the specified title
     * and it was successfully deleted
     */
    boolean deleteByTitle(String title, Connection connection);

    /**
     * Returns a number of courses in the database
     * @param connection to Database
     * @return number of courses in the database
     */
    int countCourses(Connection connection);
}
