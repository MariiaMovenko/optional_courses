package ua.nure.movenko.summaryTask4.bean;


import ua.nure.movenko.summaryTask4.entities.Course;
import ua.nure.movenko.summaryTask4.entities.User;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Provide records for virtual table formed :
 * <pre>
 * |users.id|users.firstName|users.lastName|users.email|journal.mark|courses.lector_id|courses.title_id
 * </pre>
 *
 * @author M.Movenko
 */
public class Journal {

    private Course courseInfo;
    private Map<User, String> studentsMarks = new LinkedHashMap<>();

    public Journal() {
    }

    public Journal(Map<User, String> studentsMarks) {
        this.studentsMarks = studentsMarks;
    }

    public Course getCourseInfo() {
        return courseInfo;
    }

    public void setCourseInfo(Course courseInfo) {
        this.courseInfo = courseInfo;
    }

    public void setStudentsMarks(Map<User, String> studentsMarks) {
        this.studentsMarks = studentsMarks;
    }

    public Map<User, String> getStudentsMarks() {
        return studentsMarks;
    }

    public void addRaw(User student, String mark) {
        studentsMarks.put(student, mark);
    }

}


