package ua.nure.movenko.summaryTask4.validation;

import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.UserStatus;
import ua.nure.movenko.summaryTask4.hash.Password;
import ua.nure.movenko.summaryTask4.services.course.CourseService;
import ua.nure.movenko.summaryTask4.services.dictionary.TitleDictionaryService;
import ua.nure.movenko.summaryTask4.services.theme.ThemeService;
import ua.nure.movenko.summaryTask4.services.user.UserService;

import javax.servlet.http.HttpServletRequest;

/**
 * ValidationBuilder
 *
 * @author M.Movenko
 */
public class ValidationBuilder {

    private Validator startValidator;
    private Validator currentValidator;


    public ValidationBuilder loginFormat() {
        Validator loginFormatValidator = new AbstractValidator() {
            @Override
            protected boolean executeValidation(HttpServletRequest request, ValidationResult result) {
                String login = request.getParameter(Params.LOGIN);
                if (login == null || !login.matches(Params.LOGIN_PATTERN)) {
                    result.addError(Params.LOGIN + "_error", "wrong_login_format");
                    return false;
                }
                return true;
            }
        };
        appendValidator(loginFormatValidator);
        return this;
    }

    public ValidationBuilder passwordFormat(String parameterName, boolean shouldContinue) {
        Validator passwordFormatValidator = new AbstractValidator() {
            @Override
            protected boolean executeValidation(HttpServletRequest request, ValidationResult result) {
                String password = request.getParameter(parameterName);
                if (password == null || !password.matches(Params.PASSWORD_PATTERN)) {
                    result.addError(parameterName + "_error", "wrong_password_format");
                    return shouldContinue;
                }
                return true;
            }
        };
        appendValidator(passwordFormatValidator);
        return this;
    }

    public ValidationBuilder emailFormat() {
        Validator emailFormatValidator = new AbstractValidator() {
            @Override
            protected boolean executeValidation(HttpServletRequest request, ValidationResult result) {
                String email = request.getParameter(Params.EMAIL);
                if (email == null || !email.matches(Params.EMAIL_PATTERN)) {
                    result.addError(Params.EMAIL + "_error", "wrong_email_format");
                    return false;
                }
                return true;
            }
        };
        appendValidator(emailFormatValidator);
        return this;
    }

    public ValidationBuilder titleFormat(String parameterName, boolean shouldContinue) {
        Validator titleFormatValidator = new AbstractValidator() {
            @Override
            protected boolean executeValidation(HttpServletRequest request, ValidationResult result) {
                String title = request.getParameter(parameterName);
                if (title == null || !title.matches(Params.TITLE_PATTERN)) {
                    result.addError(parameterName + "_error", "wrong_title_format");
                    return shouldContinue;
                }
                return true;
            }
        };
        appendValidator(titleFormatValidator);
        return this;
    }


    public ValidationBuilder dateFormat(String parameterName) {
        Validator dateFormatValidator = new AbstractValidator() {
            @Override
            protected boolean executeValidation(HttpServletRequest request, ValidationResult result) {
                String date = request.getParameter(parameterName);
                if (date == null || !date.matches(Params.DATE_PATTERN)) {
                    result.addError(parameterName + "_error", "wrong_date_format");
                }
                return true;
            }
        };
        appendValidator(dateFormatValidator);
        return this;
    }


    public ValidationBuilder loginExists(UserService userService, boolean shouldExist) {
        Validator loginValidator = new AbstractValidator() {
            @Override
            protected boolean executeValidation(HttpServletRequest request, ValidationResult result) {
                String login = request.getParameter(Params.LOGIN);
                boolean loginExists = userService.exists(login);
                if (shouldExist && !loginExists) {
                    result.addError(Params.LOGIN + "_error", "login_doesn't_exist");
                    return false;
                }
                if (!shouldExist == loginExists) {
                    result.addError(Params.LOGIN + "_error", "login_exists");
                }
                return true;
            }
        };

        appendValidator(loginValidator);
        return this;
    }

    public ValidationBuilder newloginExists(UserService userService) {
        Validator newLoginValidator = new AbstractValidator() {
            @Override
            protected boolean executeValidation(HttpServletRequest request, ValidationResult result) {
                String login = request.getParameter(Params.LOGIN);
                User thisUser = (User) request.getSession().getAttribute(Params.USER_INFO);
                boolean loginExists = userService.exists(login);
                if (!thisUser.getLogin().equals(login) && loginExists) {
                    result.addError(Params.LOGIN + "_error", "login_exists");
                }
                return true;
            }
        };
        appendValidator(newLoginValidator);
        return this;
    }


    public ValidationBuilder emailExists(UserService userService) {
        Validator emailValidator = new AbstractValidator() {
            @Override
            protected boolean executeValidation(HttpServletRequest request, ValidationResult result) {
                String email = request.getParameter(Params.EMAIL);
                User thisUser = (User) request.getSession().getAttribute(Params.USER_INFO);
                if (thisUser != null) {
                    if (!thisUser.getEmail().equals(email) && userService.emailExists(email)) {
                        result.addError(Params.EMAIL + "_error", "email_exists");
                    }
                } else {
                    if (userService.emailExists(email)) {
                        result.addError(Params.EMAIL + "_error", "email_exists");
                    }
                }
                return true;
            }
        };

        appendValidator(emailValidator);
        return this;
    }

    public ValidationBuilder titleExists(CourseService courseService, String parameterName) {
        Validator titleValidator = new AbstractValidator() {
            @Override
            protected boolean executeValidation(HttpServletRequest request, ValidationResult result) {
                String title = request.getParameter(parameterName);
                boolean titleExists = courseService.exists(title);
                if (titleExists) {
                    result.addError(parameterName + "_error", "title_exists");
                    return false;
                }
                return true;
            }
        };
        appendValidator(titleValidator);
        return this;
    }

    public ValidationBuilder changedTitleExists(TitleDictionaryService titleDictionaryService,
                                                String parameterName) {
        Validator titleValidator = new AbstractValidator() {
            @Override
            protected boolean executeValidation(HttpServletRequest request, ValidationResult result) {
                String title = request.getParameter(parameterName);
                int courseId = Integer.parseInt(request.getParameter("id"));
                boolean titleAlreadyInUsage = titleDictionaryService.titleAlreadyInUsage(title, courseId);
                if (titleAlreadyInUsage) {
                    result.addError(parameterName + "_error", "title_exists");
                    return false;
                }
                return true;
            }
        };
        appendValidator(titleValidator);
        return this;
    }

    public ValidationBuilder themeExists(ThemeService themeService, String parameterName) {
        Validator themeValidator = new AbstractValidator() {
            @Override
            protected boolean executeValidation(HttpServletRequest request, ValidationResult result) {
                String theme = request.getParameter(parameterName);
                boolean themeExists = themeService.exists(theme);
                if (themeExists) {
                    result.addError(parameterName + "_error", "theme_exists");
                    return false;
                }
                return true;
            }
        };
        appendValidator(themeValidator);
        return this;
    }

    public ValidationBuilder checkConfirmingPasswords(String parameterName) {
        Validator confirmPasswordValidator = new AbstractValidator() {
            @Override
            protected boolean executeValidation(HttpServletRequest request, ValidationResult result) {
                String password = request.getParameter(parameterName);
                String repeatedPassword = request.getParameter(Params.CONFIRM_PASSWORD);
                if (!password.equals(repeatedPassword)) {
                    result.addError(parameterName, "passwords_are_different");
                    result.addError(Params.CONFIRM_PASSWORD, "passwords_are_different");
                    return false;
                }
                return true;
            }
        };
        appendValidator(confirmPasswordValidator);
        return this;
    }

    public ValidationBuilder checkCurrentPassword() {
        Validator changingPasswordValidator = new AbstractValidator() {
            @Override
            protected boolean executeValidation(HttpServletRequest request, ValidationResult result) {
                String currentPassword = Password.hash(request.getParameter(Params.CURRENT_PASSWORD));
                User thisUser = (User) request.getSession().getAttribute(Params.USER_INFO);
                if (!thisUser.getPassword().equals(currentPassword)) {
                    result.addError(Params.CURRENT_PASSWORD, "wrong_password");
                    return false;
                }
                return true;
            }
        };
        appendValidator(changingPasswordValidator);
        return this;
    }

    public ValidationBuilder passwordMatchesLogin(UserService userService) {
        Validator userPasswordValidator = new AbstractValidator() {
            @Override
            protected boolean executeValidation(HttpServletRequest request, ValidationResult result) {
                String login = request.getParameter(Params.LOGIN);
                String currentPassword = Password.hash(request.getParameter(Params.PASSWORD));
                User user = userService.findUserByLogin(login);
                if (!user.getPassword().equals(currentPassword)) {
                    result.addError(Params.PASSWORD, "wrong_password");
                    return false;
                }
                request.setAttribute(Params.USER_INFO, user);
                return true;
            }
        };
        appendValidator(userPasswordValidator);
        return this;
    }

    public ValidationBuilder emailMatchesLogin(UserService userService) {
        Validator restorePasswordValidator = new AbstractValidator() {
            @Override
            protected boolean executeValidation(HttpServletRequest request, ValidationResult result) {
                String login = request.getParameter(Params.LOGIN);
                String enteredEmail = request.getParameter(Params.EMAIL);
                User user = userService.findUserByLogin(login);
                if (user == null) {
                    result.addError(Params.LOGIN_ERROR, "login_doesn't_exist");
                    return false;
                } else if (!user.getEmail().equals(enteredEmail)) {
                    result.addError(Params.EMAIL_ERROR, "wrong_email");
                    return false;
                }
                request.setAttribute(Params.USER_INFO, user);
                return true;
            }
        };
        appendValidator(restorePasswordValidator);
        return this;
    }

    public ValidationBuilder userNotActive() {
        Validator loginFormatValidator = new AbstractValidator() {
            @Override
            protected boolean executeValidation(HttpServletRequest request, ValidationResult result) {
                User user = (User) request.getAttribute(Params.USER_INFO);
                if (user.getStatus().equals(UserStatus.BANNED)) {
                    result.addError(UserStatus.BANNED.name(), "is_banned");
                    return false;
                } else if (user.getStatus().equals(UserStatus.PENDING)) {
                    result.addError(UserStatus.PENDING.name(), "registration_not_finished");
                    return false;
                }
                return true;
            }
        };
        appendValidator(loginFormatValidator);
        return this;
    }

    public Validator build() {
        return startValidator;
    }

    private void appendValidator(Validator validator) {
        if (startValidator == null) {
            startValidator = validator;
            currentValidator = validator;
            return;
        }
        currentValidator.next(validator);
        currentValidator = validator;
    }
}
