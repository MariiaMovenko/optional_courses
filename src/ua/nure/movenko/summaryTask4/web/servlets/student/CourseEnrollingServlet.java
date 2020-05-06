package ua.nure.movenko.summaryTask4.web.servlets.student;

import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.services.journal.JournalService;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * End point of this servlet is to enroll student to chosen by him/her course
 */
@WebServlet("/enroll")
public class CourseEnrollingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private JournalService journalService;

    @Override
    public void init() {
        journalService = (JournalService) getServletContext().getAttribute("JOURNAL_SERVICE");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String[] courseTitles = request.getParameterValues(Params.TITLE);
        User student = (User) request.getSession().getAttribute(Params.USER_INFO);
        boolean isSubscribed = journalService.enrollStudent(student, courseTitles);
        if (!isSubscribed) {
            response.setStatus(304);
        }
        response.setStatus(200);
    }
}
