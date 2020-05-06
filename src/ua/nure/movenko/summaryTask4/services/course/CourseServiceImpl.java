package ua.nure.movenko.summaryTask4.services.course;

import ua.nure.movenko.summaryTask4.db.transaction.TransactionManager;
import ua.nure.movenko.summaryTask4.db.dao.course.CourseDAO;
import ua.nure.movenko.summaryTask4.db.dao.dictionary.TitleDictionaryDao;
import ua.nure.movenko.summaryTask4.entities.Course;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.CourseStatus;
import ua.nure.movenko.summaryTask4.models.CourseModel;
import ua.nure.movenko.summaryTask4.models.StudentCourseModel;
import ua.nure.movenko.summaryTask4.util.ViewConfiguration;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CourseServiceImpl based  on implementation of the {@code CourseService} interface.
 *
 *  @ author M.Movenko
 */
public class CourseServiceImpl implements CourseService {
    private TransactionManager transactionManager;
    private CourseDAO courseDAO;
    private TitleDictionaryDao titleDictionaryDao;

    public CourseServiceImpl(TransactionManager transactionManager, CourseDAO courseDAO, TitleDictionaryDao titleDictionaryDao) {
        this.transactionManager = transactionManager;
        this.courseDAO = courseDAO;
        this.titleDictionaryDao = titleDictionaryDao;
    }

    @Override
    public Course getCourseById(int courseId) {
        return transactionManager
                .execute(connection -> courseDAO.getById(courseId, connection), Connection.TRANSACTION_NONE);
    }

    @Override
    public List<CourseModel> getCoursesByLectorSurname(ViewConfiguration params, String lectorSurname) {
        return transactionManager.execute(connection ->
                courseDAO.getCoursesByLectorSurname(params, lectorSurname, connection).stream()
                        .map(course -> convertToModel(course, params.getLanguage()))
                        .collect(Collectors.toList()), Connection.TRANSACTION_NONE);
    }

    @Override
    public List<CourseModel> getCoursesByThemeAndLector(ViewConfiguration params, String themeTitle,
                                                        String lectorSurname) {
        return transactionManager.execute(connection ->
                courseDAO.getCoursesByLectorSurnameAndTheme(params, themeTitle, lectorSurname, connection).stream()
                        .map(course -> convertToModel(course, params.getLanguage()))
                        .collect(Collectors.toList()), Connection.TRANSACTION_NONE);
    }

    @Override
    public List<CourseModel> getCoursesByTheme(ViewConfiguration params, String themeTitle) {
        return transactionManager.execute(connection -> courseDAO.getCoursesByTheme(params, themeTitle, connection)
                .stream().map(course -> convertToModel(course, params.getLanguage()))
                .collect(Collectors.toList()), Connection.TRANSACTION_NONE);
    }


    @Override
    public List<CourseModel> getAllCourses(String language) {
        return transactionManager.execute(connection -> courseDAO.getAll(connection)
                .stream().map(course -> convertToModel(course, language))
                .collect(Collectors.toList()), Connection.TRANSACTION_NONE);
    }

    @Override
    public List<CourseModel> getAllCoursesOrdered(ViewConfiguration params) {
        return transactionManager.execute(connection -> courseDAO.getAllCoursesOrdered(connection, params)
                .stream()
                .map(course -> convertToModel(course, params.getLanguage()))
                .collect(Collectors.toList()), Connection.TRANSACTION_NONE);
    }

    @Override
    public List<StudentCourseModel> getStudentCourses(User student, CourseStatus status, String language) {
        return transactionManager.execute(connection ->
                (filterCourseByStatus(courseDAO.getStudentCourses(student, connection), status)
                        .stream().map(course -> convertToStudentView(course, student, language))
                        .collect(Collectors.toList())), Connection.TRANSACTION_NONE);
    }

    @Override
    public List<CourseModel> getLectorCourses(User lector, CourseStatus status, String language) {
        return transactionManager.execute(connection ->
                (filterCourseByStatus(courseDAO.getLectorCourses(lector, connection), status)
                        .stream().map(course -> convertToModel(course, language))
                        .collect(Collectors.toList())), Connection.TRANSACTION_NONE);
    }

    @Override
    public void addCourse(Course course) {
        transactionManager.execute(connection -> courseDAO.add(setDictionaryId(course, connection),
                connection), Connection.TRANSACTION_REPEATABLE_READ);
    }


    @Override
    public void deleteCourse(String title) {
        transactionManager.execute(connection -> courseDAO.deleteByTitle(title, connection),
                Connection.TRANSACTION_NONE);
    }

    @Override
    public void updateCourse(Course course) {
        transactionManager
                .execute(connection -> courseDAO.update(course, connection),
                        Connection.TRANSACTION_REPEATABLE_READ);
    }

    @Override
    public boolean exists(String title) {
        return transactionManager
                .execute(connection -> courseDAO.exists(title, connection),
                        Connection.TRANSACTION_NONE);
    }

    @Override
    public int countCourses() {
        return  transactionManager.execute(connection -> courseDAO.countCourses(connection),
                Connection.TRANSACTION_NONE);
    }

    private CourseModel convertToModel(Course course, String language) {
        CourseModel model = new CourseModel();
        fillModel(course, model, language);
        return model;
    }

    private StudentCourseModel convertToStudentView(Course course, User student, String language) {
        StudentCourseModel courseModel = new StudentCourseModel();
        fillModel(course, courseModel, language);
        courseModel.setLectorEmail(course.getLector().getEmail());
        if (courseModel.getStatus().equals(CourseStatus.FINISHED)) {
            courseModel.setMark(course.getJournal().getStudentsMarks().get(student));
        }
        return courseModel;
    }


    private List<Course> filterCourseByStatus(List<Course> courses, CourseStatus status) {
        return courses.stream()
                .filter(course -> course.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    private String  defineLectorInfo(User lector) {
        if (lector != null) {
            return lector.getFirstName() + " " + lector.getLastName();
        } else {
            return "";
        }
    }

    private Course setDictionaryId(Course course, Connection connection) {
        course.getTitleDictionary().setId(titleDictionaryDao
                .add(course.getTitleDictionary(), connection));
        return course;
    }

    private void fillModel(Course course, CourseModel model, String language) {
        model.setId(course.getId());
        model.setEnTitle(course.getTitleDictionary().getEn());
        model.setRuTitle(course.getTitleDictionary().getRu());
        if ("en".equalsIgnoreCase(language)) {
            model.setTitle(course.getTitleDictionary().getEn());
            model.setTheme(course.getTheme().getThemeTitleEn());
        } else {
            model.setTitle(course.getTitleDictionary().getRu());
            model.setTheme(course.getTheme().getThemeTitleRu());
        }
        model.setLector(defineLectorInfo(course.getLector()));
        model.setStartDate(course.getStartDate().toLocalDate().toString());
        model.setEndDate(course.getFinishDate().toLocalDate().toString());
        model.setDuration(course.getDuration());
        model.setDescription(course.getDescription());
        model.setStudentsEnrolled(course.getStudentsEnrolled());
        model.setEnrolled(course.isUserEnrolled());
        model.setStatus(course.getStatus());
    }
}
