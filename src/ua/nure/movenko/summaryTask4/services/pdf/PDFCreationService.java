package ua.nure.movenko.summaryTask4.services.pdf;

import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import ua.nure.movenko.summaryTask4.bean.Journal;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.services.journal.JournalService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;


public class PDFCreationService {

    private static final String CP1251 = "cp1251";

    private static final String FONT_PATH = "C:\\Windows\\Fonts\\times.ttf";
    JournalService journalService;

    public PDFCreationService(JournalService journalService) {
        this.journalService = journalService;
    }

    public ByteArrayOutputStream createPdfReport(User lector, String title, int courseId, String language) throws DocumentException, IOException {
        Journal journal = journalService.getCourseJournal(lector, courseId );
        String student = (language.equalsIgnoreCase("en") ? "STUDENT" : "СТУДЕНТ");
        String studentEmail = (language.equalsIgnoreCase("en") ?"EMAIL":"ЭЛЕКТРОННЫЙ АДРЕС");
        String mark = (language.equalsIgnoreCase("en")?"MARK": "ОЦЕНКА");
        BaseFont baseFont = BaseFont.createFont(FONT_PATH, CP1251, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 14, Font.NORMAL);
        Font titleFont = new Font(baseFont, 16, Font.BOLD);
        Paragraph preface = new Paragraph(new Paragraph(title.toUpperCase(), titleFont));
        preface.setAlignment(Element.ALIGN_CENTER);

        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();
        document.add(preface);
        document.add(new Paragraph("  "));

        PdfPTable table = new PdfPTable(3);
        table.addCell(new Phrase(student, font));
        table.addCell(new Phrase(studentEmail, font));
        table.addCell(new Phrase(mark, font));

        Map<User, String> journalRaws = journal.getStudentsMarks();
        journalRaws.forEach((key, value) -> setDataToTable(table, key, font, value));

        document.add(table);
        document.close();

        return byteArrayOutputStream;
    }

    private void setDataToTable(PdfPTable table, User user, Font font, String mark) {
        table.addCell(new Phrase(user.getLastName().concat(" ").concat(user.getFirstName()), font));
        table.addCell(new Phrase(user.getEmail(), font));
        table.addCell(new Phrase(mark, font));
    }
}
