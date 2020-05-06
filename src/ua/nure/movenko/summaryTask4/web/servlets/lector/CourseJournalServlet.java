package ua.nure.movenko.summaryTask4.web.servlets.lector;

import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.bean.Journal;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.services.journal.JournalService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Retrieves the list of students (and their marks) of the particular course of the particular lecturer
 */
@WebServlet("/show_journal")
public class CourseJournalServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private JournalService journalService;

    @Override
    public void init() {
        journalService = (JournalService) getServletContext().getAttribute("JOURNAL_SERVICE");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer courseId = Integer.parseInt(request.getParameter(Params.COURSE_ID));
        String courseFinished = Optional.ofNullable(request.getParameter(Params.COURSE_FINISHED))
                .orElse("false");

        User lector = (User) request.getSession().getAttribute(Params.USER_INFO);
        Journal journal = journalService.getCourseJournal(lector, courseId);
        request.setAttribute(Params.JOURNAL, journal);
        request.setAttribute(Params.COURSE_FINISHED, courseFinished);
        request.getRequestDispatcher("/journal.html").forward(request, response);
    }
}
