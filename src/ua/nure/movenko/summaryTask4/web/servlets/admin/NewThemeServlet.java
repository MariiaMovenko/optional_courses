package ua.nure.movenko.summaryTask4.web.servlets.admin;

import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.entities.Theme;
import ua.nure.movenko.summaryTask4.enums.ValidationStatus;
import ua.nure.movenko.summaryTask4.services.theme.ThemeService;
import ua.nure.movenko.summaryTask4.util.ParamParser;
import ua.nure.movenko.summaryTask4.validation.ValidationBuilder;
import ua.nure.movenko.summaryTask4.validation.ValidationResult;
import ua.nure.movenko.summaryTask4.validation.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * End point of this servlet is to create a new subject
 */
@WebServlet("/add_theme")
public class NewThemeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ThemeService themeService;
    private Validator validator;

    @Override
    public void init() {
        themeService = (ThemeService) getServletContext().getAttribute("THEME_SERVICE");
        validator = new ValidationBuilder()
                .titleFormat(Params.EN_THEME_TITLE, false)
                .titleFormat(Params.RU_THEME_TITLE, false)
                .themeExists(themeService, Params.EN_THEME_TITLE)
                .themeExists(themeService, Params.RU_THEME_TITLE)
                .build();

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ValidationResult validationResult = validator.validate(request);
        if (validationResult.getValidationStatus() == ValidationStatus.SUCCESS) {
            Theme theme = ParamParser.parseTheme(request);
            themeService.addTheme(theme);
            response.sendRedirect("main.html");
        } else {
            response.sendRedirect("add-theme.html?" + validationResult.toURL());
        }
    }
}
