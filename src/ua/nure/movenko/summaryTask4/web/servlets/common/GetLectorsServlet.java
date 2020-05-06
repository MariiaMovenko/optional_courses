package ua.nure.movenko.summaryTask4.web.servlets.common;

import com.google.gson.Gson;
import ua.nure.movenko.summaryTask4.models.DropdownListItemModel;
import ua.nure.movenko.summaryTask4.models.LectorModel;
import ua.nure.movenko.summaryTask4.services.user.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Retrieves the list of all registered lecturers
 */
@WebServlet("/get_lectors")
public class GetLectorsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserService userService;

    @Override
    public void init() {
        userService = (UserService) getServletContext().getAttribute("USER_SERVICE");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<DropdownListItemModel> lectors = userService.getAllLectors().stream()
                .map(this::adaptToDropdownItemModel)
                .collect(Collectors.toList());
        String json = new Gson().toJson(lectors);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    private DropdownListItemModel adaptToDropdownItemModel(LectorModel lectorModel) {
        return new DropdownListItemModel(
                lectorModel.getFirstName().concat(" ").concat(lectorModel.getLastName()));
    }
}
