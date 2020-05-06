package ua.nure.movenko.summaryTask4.web.servlets.common;

import com.google.gson.Gson;
import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.models.DropdownListItemModel;
import ua.nure.movenko.summaryTask4.models.ThemeModel;
import ua.nure.movenko.summaryTask4.services.theme.ThemeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * End point of this servlet is to retrieve the list existing themes for further using
 */
@WebServlet("/all_themes")
public class AllThemesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ThemeService themeService;

    @Override
    public void init() {
        themeService = (ThemeService) getServletContext().getAttribute("THEME_SERVICE");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String language = Optional.ofNullable(request.getSession())
                .map(session -> session.getAttribute(Params.LANGUAGE))
                .map(Object::toString)
                .map(locale -> locale.substring(0, 2))
                .orElse("ru");
        List<DropdownListItemModel> themeModelList = themeService.getAllThemes(language).stream()
                .map(this::adaptToDropdownItemModel)
                .collect(Collectors.toList());
        String json = new Gson().toJson(themeModelList);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    private DropdownListItemModel adaptToDropdownItemModel(ThemeModel themeModel) {
        return new DropdownListItemModel(themeModel.getThemeTitle());
    }

}


