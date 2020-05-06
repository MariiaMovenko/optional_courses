package ua.nure.movenko.summaryTask4.web.servlets.supporting;

import org.apache.log4j.Logger;
import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.entities.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Retrieves an avatar image of the particular user for further displaying
 */
@WebServlet("/find_image")
public class ImageLoadServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ImageLoadServlet.class);
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User thisUser = (User) request.getSession().getAttribute(Params.USER_INFO);
        byte[] imageBytes = Files.readAllBytes(new File(thisUser.getAvatarPath()).toPath());

        response.setContentType("image/jpeg");
        response.setContentLength(imageBytes.length);

        response.getOutputStream().write(imageBytes);
    }
}
