package ua.nure.movenko.summaryTask4.services.journal;

import ua.nure.movenko.summaryTask4.bean.Journal;
import ua.nure.movenko.summaryTask4.entities.User;

import java.util.List;

public interface JournalService {
    /**
     * This method binds a particular student {@param student} to  the course(s) the specified {@param courseTitle}
     *
     * @param student      defines a {@code student.getUserId()} id of the user who enrolls to the courses.
     * @param courseTitle defines title of the course student enrolls to
     * @return {@code true} if there were thrown no exceptions during execution of this method
     */
    boolean enrollStudent(User student, String courseTitle);

    /**
     * Returns an object that represents students of the particular course and their marks as a {@code Map<User, String>}
     *
     * @param lector      is criteria to find the particular course
     * @param courseId like the {@param Lector} is a criteria to find the particular course
     * @return object that represents students of the particular course and their marks as a {@code Map<User, String>}
     */
    Journal getCourseJournal(User lector, Integer courseId);

    /**
     * Method changes mark(s) of  particular student(s) of the specified course
     *
     * @param studentId defines users whose mark should be updated
     * @param mark      new mark(s) to be saved
     * @param courseId    is id of the course to which student with the particular  {@param studentId} enrolled
     */
    void updateJournal(List<Integer> studentId, List<String> mark, Integer courseId);
}
