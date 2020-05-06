package ua.nure.movenko.summaryTask4.web.servlets.lector;

import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.services.journal.JournalService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Updates information on students' marks in the journal
 */
@WebServlet("/update_journal")
public class UpdateJournalServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private JournalService journalService;

    @Override
    public void init() {
        journalService = (JournalService) getServletContext().getAttribute("JOURNAL_SERVICE");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Integer courseId = Integer.parseInt(request.getParameter(Params.COURSE_ID));
        List<Integer> studentId = Arrays.stream(request.getParameterValues(Params.STUDENT_ID)).map(Integer::parseInt).collect(Collectors.toList());
        List<String> marks = Arrays.asList(request.getParameterValues(Params.MARK));
        journalService.updateJournal(studentId, marks, courseId);
        request.getSession().setAttribute("updated", "updated");
        response.sendRedirect("show_journal?course_id=" + courseId);
    }
}
