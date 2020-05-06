package ua.nure.movenko.summaryTask4.entities;

import ua.nure.movenko.summaryTask4.bean.Journal;
import ua.nure.movenko.summaryTask4.enums.CourseStatus;

import java.sql.Date;
/**
 * Course entity.
 *
 * @author M.Movenko
 */
public class Course extends Entity {

    private TitleDictionary titleDictionary;
    private Theme theme;
    private User lector;
    private Date startDate;
    private Date finishDate;
    private int duration;
    private int studentsEnrolled;
    private CourseStatus status;
    private String description;
    private Journal journal;
    private  boolean userEnrolled;

    public Course(){}

    public TitleDictionary getTitleDictionary() {
        return titleDictionary;
    }

    public void setTitleDictionary(TitleDictionary titleDictionary) {
        this.titleDictionary = titleDictionary;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public User getLector() {
        return lector;
    }

    public void setLector(User lector) {
        this.lector = lector;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public int getStudentsEnrolled() {
        return studentsEnrolled;
    }

    public void setStudentsEnrolled(int studentsEnrolled) {
        this.studentsEnrolled = studentsEnrolled;
    }

    public void setStatus(CourseStatus status) {

        this.status = status;
    }

    public Journal getJournal() {
        return journal;
    }

    public void setJournal(Journal journal)
    {
        this.journal = journal;
    }

    public boolean isUserEnrolled() {
        return userEnrolled;
    }

    public void setUserEnrolled(boolean userEnrolled) {
        this.userEnrolled = userEnrolled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
