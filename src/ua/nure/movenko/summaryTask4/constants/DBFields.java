package ua.nure.movenko.summaryTask4.constants;

/**
 * Holder for fields names of DB tables and beans.
 *
 * @author M.Movenko
 */
public final class DBFields {

    public static final String ENTITY_ID = "id";

    // Fields for stdb.users;
    public static final String USER_LOGIN = "login";
    public static final String USER_PASSWORD = "password";
    public static final String USER_EMAIL = "email";
    public static final String USER_FIRST_NAME = "first_name";
    public static final String USER_LAST_NAME = "last_name";
    public static final String USER_ROLE_ID = "role_id";
    public static final String USER_STATUS_ID = "status_id";
    public static final String PATH_TO_AVATAR = "avatar";

    // Fields for stdb.courses;
    public static final String TITLE_ID = "title_id";
    public static final String THEME_ID = "theme_id";
    public static final String LECTOR_ID = "lector_id";
    public static final String START_DATE = "start_date";
    public static final String FINISH_DATE = "finish_date";
    public static final String DURATION = "duration";
    public static final String DESCRIPTION = "description";
    public static final String NUMBER_OF_STUDENTS = "students_enrolled";

    // Fields for stdb.themes
    public static final String THEME_EN = "en_theme";
    public static final String THEME_RU = "ru_theme";

    // Fields for stdb.title_dictionary
    public static final String TITLE_EN = "en";
    public static final String TITLE_RU = "ru";

    // Fields for stdb.journal;
    public static final String MARK = "mark";
    public static final String STUDENT_ID = "student_id";
    public static final String COURSE_ID = "course_id";

    // Virtual field, gives information either particular student is enrolled to tha particular course or not;
    public static final String ENROLLED = "enrolled";

}
