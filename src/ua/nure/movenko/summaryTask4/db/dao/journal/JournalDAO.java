package ua.nure.movenko.summaryTask4.db.dao.journal;

import ua.nure.movenko.summaryTask4.bean.Journal;
import ua.nure.movenko.summaryTask4.entities.User;

import java.sql.Connection;
import java.util.List;

public interface JournalDAO {

    /**
     * This method binds a particular student {@param student} to  the course(s) the specified {@param courseTitle}
     * in the Database
     *
     * @param student      defines a {@code student.getUserId()} id of the user who enrolls to the courses.
     * @param connection   to Database
     * @param courseTitle defines title of the course student enrolls to
     * @return {@code true} if there were thrown no exceptions during execution of this method
     */
    boolean enrollStudent(User student, Connection connection, String courseTitle);

    /**
     * Returns an object that represents students of the particular course and their marks as a {@code Map<User, String>}
     *
     * @param connection  to Database
     * @param lector      is criteria to find the particular course
     * @param courseId like the {@param Lector} is a criteria to find the particular course
     * @return object that represents students of the particular course and their marks as a {@code Map<User, String>}
     */
    Journal getCourseJournal(Connection connection, User lector, Integer courseId);

    /**
     * Method changes mark of the particular student of the specified course
     *
     * @param studentId  defines the particular {@code User user} who enrolled to the course with {@param courseTitle}
     *                   and has field {@param mark}
     * @param mark       is parameter to be changed
     * @param courseId     is id of the course to which student with the particular  {@param studentId} enrolled
     * @param connection to Database
     * @return {@code true} if there were thrown no exceptions during execution of this method
     */
    boolean updateJournal(List<Integer> studentId, List<String> mark, Integer courseId, Connection connection);
}
