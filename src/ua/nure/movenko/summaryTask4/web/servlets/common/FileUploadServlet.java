package ua.nure.movenko.summaryTask4.web.servlets.common;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.services.user.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Uploads avatar images to the chosen directory and removes previous avatar image
 * of the particular user
 */
@WebServlet("/upload_file")
public class FileUploadServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(FileUploadServlet.class);
    private final static String UPLOAD_DIRECTORY = "C:\\Users\\nefre\\IdeaProjects\\projectUploads";
    private static String FULL_PATH_TO_FILE;
    private UserService userService;

    private final List<String> allowedPatterns = new ArrayList<>() {{
        add(".*\\.png");
        add(".*\\.jpg");
    }};

    public FileUploadServlet() {
        super();
    }

    @Override
    public void init() {
        userService = (UserService) getServletContext().getAttribute("USER_SERVICE");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User thisUser = (User) request.getSession().getAttribute(Params.USER_INFO);
        if (saveUploadedFile(request, response, thisUser)) {
            clearPreviousAvatar(thisUser);
            thisUser.setAvatarPath(FULL_PATH_TO_FILE);
            userService.changeAvatar(thisUser);
            request.getSession().setAttribute(Params.USER_INFO, thisUser);
            response.sendRedirect("my_profile.html");
        } else {
            response.sendRedirect("upload_file.html?upload_error=" + "not_uploaded");
        }
    }

    private void clearPreviousAvatar(User user) {
        String avatarPath = user.getAvatarPath();
        if (avatarPath != null) {
            try {
                Files.deleteIfExists(Path.of(avatarPath));
            } catch (IOException e) {
                LOG.error("Can't remove previous avatar", e);
            }
        }
    }

    private boolean saveUploadedFile(HttpServletRequest request, HttpServletResponse response, User user) {
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                FileItem item = new ServletFileUpload(
                        new DiskFileItemFactory()).parseRequest(request).get(0);
                if (allowedPatterns.stream().anyMatch(item.getName().toLowerCase()::matches)) {
                    FULL_PATH_TO_FILE = userService.uploadFile(item, user, UPLOAD_DIRECTORY);
                    return true;
                }
                LOG.trace("Uploaded file has inappropriate extension. Uploading was rejected.");
            } catch (Exception e) {
                LOG.error("Can't upload file to the system", e);
            }
        }
        return false;
    }
}
