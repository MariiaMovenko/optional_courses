package ua.nure.movenko.summaryTask4.web.servlets.lector;

import com.lowagie.text.DocumentException;
import org.apache.log4j.Logger;
import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.services.pdf.PDFCreationService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

/**
 * Creates a pdf-representation of the particular journal
 */
@WebServlet("/journal/report")
public class JournalReportServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(JournalReportServlet.class);
    private PDFCreationService pdfCreationService;

    @Override
    public void init() {
        pdfCreationService = (PDFCreationService) getServletContext().getAttribute("PDF_SERVICE");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String courseTitle = request.getParameter(Params.TITLE);
        int courseId = Integer.parseInt(request.getParameter(Params.COURSE_ID));
        User lector = (User) request.getSession().getAttribute(Params.USER_INFO);
        String language =  Optional.ofNullable(request.getSession())
                .map(session -> session.getAttribute(Params.LANGUAGE))
                .map(Object::toString)
                .map(locale -> locale.substring(0, 2))
                .orElse("ru");

        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = pdfCreationService.createPdfReport(lector, courseTitle, courseId, language);
        } catch (DocumentException e) {
            LOG.error("Document exception has been occurred ", e);
        }

        if (byteArrayOutputStream != null) {
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control",
                    "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            response.setContentType("journal/pdf");
            response.setContentLength(byteArrayOutputStream.size());
            OutputStream os = response.getOutputStream();
            byteArrayOutputStream.writeTo(os);
            os.flush();
            os.close();
        }
    }

}
