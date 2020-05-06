package ua.nure.movenko.summaryTask4.services.course;

import ua.nure.movenko.summaryTask4.entities.Course;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.CourseStatus;
import ua.nure.movenko.summaryTask4.models.CourseModel;
import ua.nure.movenko.summaryTask4.models.StudentCourseModel;
import ua.nure.movenko.summaryTask4.util.ViewConfiguration;


import java.util.List;

public interface CourseService {

    /**
     * Returns  course by it's identical number.
     *
     * @param courseId is id of course that should be returned
     * @return course with specified id or null if there is'nt any course with  particular {@code courseId}
     */
    Course getCourseById(int courseId);

    /**
     * Returns  adapted to view layer list of courses of the specified Lecturer and on the specified subject.
     *
     * @param params        represents an util class object which contains information concerning view layer
     *                      such as chosen locale, limit courses on the page, page to show
     * @param lectorSurname is criteria for selecting courses to return
     * @return list of courses of specified  lecturer
     */

    List<CourseModel> getCoursesByLectorSurname(ViewConfiguration params, String lectorSurname);

    /**
     * Returns adapted to view layer list of courses of a particular lecturer and   on the specified subject.
     *
     * @param params        represents an util class object which contains information concerning view layer
     *                      such as chosen locale, limit courses on the page, page to show
     * @param lectorSurname is criteria for choosing courses to return
     * @return list of courses of a particular lecturer and on the specified subject
     */
    List<CourseModel> getCoursesByThemeAndLector(ViewConfiguration params, String themeTitle, String lectorSurname);

    /**
     * Returns adapted to view layer list of courses on the specified subject.
     *
     * @param params  represents an util class object which contains information concerning view layer such
     *                as chosen locale, limit courses on the page, page to show
     * @param subject is criteria for choosing courses to return
     * @return Returns adapted to view layer list of courses on the specified subject.
     */
    List<CourseModel> getCoursesByTheme(ViewConfiguration params, String subject);

    /**
     * Finds and returns list courses' models sorted by title in Ascending order
     * it adapted to view layer
     *
     * @param language defines view layer language information on returned courses to be translated to
     * @return Returns list of models sorted in the specific  order which defined in  {@code params}
     */
    List<CourseModel> getAllCourses(String language);

    /**
     * Finds and returns all courses in the specific order defined in the {@code params}
     *
     * @param params represents an util class object which contains information concerning view layer such as chosen locale, limit courses on the page,
     *               page to show, courses order to show and   order criteria
     * @return Returns adapted to view layer list of courses sorted in the specific  order which defined in  {@code params}
     */
    List<CourseModel> getAllCoursesOrdered(ViewConfiguration params);

    /**
     * Returns list of courses with {@code CourseStatus status} that  particular {@code student.getUserId()} enrolled to
     *
     * @param status   defines {@code CourseStatus status}  of seeked courses
     * @param student  is a user by whose id courses to return should be found
     * @param language defines view layer language information on returned courses to be translated to
     * @return list adapted to view layer of courses particular student enrolled to
     */
    List<StudentCourseModel> getStudentCourses(User student, CourseStatus status, String language);

    /**
     * Returns list of courses  that particular {@code lector.getUserId()} teaches
     *
     * @param status   defines  the status of seeked courses
     * @param lector   is a user by whose id courses to return should be found
     * @param language defines view layer language information on returned courses to be translated to
     * @return list of courses' models particular {@code lector.getUserId()} teaches
     */
    List<CourseModel> getLectorCourses(User lector, CourseStatus status, String language);

    /**
     * Method adds and saves  new {@code course} in the system
     *
     * @param course an object to save
     */
    void addCourse(Course course);

    /**
     * Removes course from the system  by  it's {@code title}
     *
     * @param title defines the title of the element  to be deleted
     */
    void deleteCourse(String title);

    /**
     * Method changes information on the {@code course}  depending on {@code course} parameters
     *
     * @param course contains parameters to  update
     */
    void updateCourse(Course course);

    /**
     * Check if  the course with the particular {@param title} exists
     *
     * @param title of chosen course
     * @return {@code true} if the course with the specified title exists
     */
    boolean exists(String title);

    /**
     * Returns a number of  existing courses
     *
     * @return number of existing courses
     */
    int countCourses();

}
