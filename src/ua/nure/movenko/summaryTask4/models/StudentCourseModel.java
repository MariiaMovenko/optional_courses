package ua.nure.movenko.summaryTask4.models;

/**
 *Holds information on the course particular student has been enrolled to and student's mark.
 *
 * @author M.Movenko
 *
 */
public class StudentCourseModel extends CourseModel {

    private String lectorEmail;
    private String mark;

    public String getLectorEmail() {
        return lectorEmail;
    }

    public void setLectorEmail(String lectorEmail) {
        this.lectorEmail = lectorEmail;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

}
