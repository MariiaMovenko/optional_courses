package ua.nure.movenko.summaryTask4.constants;

/**
 * Holder for request parameters' names.
 */
public final class Params {

    public static final String ID = "id";
    public static final String LOGIN = "login";
    public static final String LOGIN_PATTERN = "^[A-Za-z0-9]{3,20}$";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String ROLE = "role";
    public static final String EMAIL = "email";
    public static final String EMAIL_PATTERN = "[a-z0-9.0_%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$";
    public static final String PASSWORD = "password";
    public static final String PASSWORD_PATTERN = "^[A-Za-z0-9]{4,10}$";
    public static final String CONFIRM_PASSWORD = "repeatedPassword";
    public static final String USER_INFO = "userInfo";
    public static final String NEW_PASSWORD = "new_password";
    public static final String CURRENT_PASSWORD = "current_password";
    public static final String PATH_TO_AVATAR = "avatar";
    public static final String CONFIRMATION_CODE = "confirmation_code";

    public static final String COURSE_ID = "course_id";
    public static final String TITLE = "title";
    public static final String TITLE_PATTERN = "^[A-Z\u0410-\u042f]+.*";
    public static final String EN_COURSE_TITLE = "title_en";
    public static final String RU_COURSE_TITLE = "title_ru";
    public static final String START_DATE = "start_date";
    public static final String FINISH_DATE = "finish_date";
    public static final String DATE_PATTERN = "^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$";
    public static final String THEME_TITLE = "theme_title";
    public static final String RU_THEME_TITLE = "ru_theme";
    public static final String EN_THEME_TITLE = "en_theme";
    public static final String LECTOR_INFO = "lector_name";
    public static final String DESCRIPTION = "description";

    public static final String EN_COURSE_TITLE_ERROR = "title_en_error";
    public static final String RU_COURSE_TITLE_ERROR = "title_ru_error";
    public static final String START_DATE_ERROR = "start_date_error";
    public static final String FINISH_DATE_ERROR = "finish_date_error";
    public static final String RU_THEME_ERROR = "ru_theme_error";
    public static final String EN_THEME_ERROR = "en_theme_error";
    public static final String EMAIL_ERROR = "email_error";
    public static final String LOGIN_ERROR = "login_error";

    public static final String LIMIT = "limit";
    public static final String PAGE = "page";
    public static final String STATUS = "status";
    public static final String LANGUAGE = "language";
    public static final String OPERATION = "operation";
    public static final String SORT_CRITERIA = "criteria";
    public static final String SORT_ORDER = "order";

    public static final String JOURNAL = "journal";
    public static final String COURSE_FINISHED = "courseFinished";
    public static final String MARK = "mark";
    public static final String STUDENT_ID = "student_id";

}
