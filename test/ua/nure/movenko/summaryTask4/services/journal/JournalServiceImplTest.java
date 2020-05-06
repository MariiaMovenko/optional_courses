package ua.nure.movenko.summaryTask4.services.journal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.nure.movenko.summaryTask4.bean.Journal;
import ua.nure.movenko.summaryTask4.db.dao.journal.JournalDAO;
import ua.nure.movenko.summaryTask4.db.transaction.TransactionManagerMock;
import ua.nure.movenko.summaryTask4.entities.Course;
import ua.nure.movenko.summaryTask4.entities.Theme;
import ua.nure.movenko.summaryTask4.entities.TitleDictionary;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.Role;
import ua.nure.movenko.summaryTask4.enums.UserStatus;

import java.sql.Connection;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JournalServiceImplTest {

    @Mock
    private JournalDAO journalDAO;

    private final TransactionManagerMock transactionManager = new TransactionManagerMock();

    @InjectMocks
    private final JournalService journalService = new JournalServiceImpl(transactionManager, journalDAO);

    @Test
    public void shouldEnrollStudent() throws Exception {
        User student = createUser(Role.STUDENT, UserStatus.ACTIVE);
        String[] courseTitle = {"Title1", "Title2"};
        when(journalDAO.enrollStudent(student, transactionManager.getConnectionForTest(), courseTitle)).thenReturn(true);

        boolean enrolled = journalService.enrollStudent(student, courseTitle);
        Assert.assertTrue(enrolled);
        transactionManager.verifyExecutedInTransaction(Connection.TRANSACTION_REPEATABLE_READ);

    }

    @Test
    public void shouldGetCourseJournal() throws Exception {
        User lector = createUser(Role.LECTOR, UserStatus.ACTIVE);
        Journal journal = createJournal();
        int courseId = 1;
        when(journalDAO.getCourseJournal(transactionManager.getConnectionForTest(), lector, courseId))
                .thenReturn(journal);

        Journal result = journalService.getCourseJournal(lector, courseId);

        Assert.assertEquals( 1, result.getStudentsMarks().size());
        transactionManager.verifyExecutedWithoutTransaction();
    }

    @Test
    public void shouldUpdateJournal() throws Exception {
        User student = createUser(Role.STUDENT, UserStatus.ACTIVE);
        int  courseId = 1;
        List<String> marks = List.of("B");
        List<Integer> studentsId = List.of(student.getId());
        when(journalDAO.updateJournal(studentsId, marks, courseId, transactionManager.getConnectionForTest()))
                .thenReturn(true);

        journalService.updateJournal(studentsId, marks, courseId);
        transactionManager.verifyExecutedInTransaction(Connection.TRANSACTION_REPEATABLE_READ);
    }

    private User createUser(Role role, UserStatus status) {
        User user = new User();
        user.setId(1);
        user.setLogin("login");
        user.setPassword("password");
        user.setRole(role);
        user.setLastName("Petrov");
        user.setFirstName("Ivan");
        user.setEmail("test@gmail.com");
        user.setStatus(status);
        return user;
    }

    private Journal createJournal() {
        Journal journal = new Journal();
        journal.setCourseInfo( createCourse());
        User student = createUser(Role.STUDENT, UserStatus.BANNED);
        student.setLogin("studentLogin");
        Map<User, String> journalRaws = new HashMap<>();
        journalRaws.put(student, "b");
        journal.setStudentsMarks(journalRaws);
        return journal;
    }

    private static Course createCourse() {
        Course course = new Course();
        course.setTitleDictionary(new TitleDictionary("Title", "Название"));
        course.getTitleDictionary().setId(1);
        course.setTheme(new Theme("Maths","Математика"));
        User lector = new User();
        lector.setFirstName("FN");
        lector.setLastName("LN");
        course.setLector(lector);
        course.setStartDate(Date.valueOf("2020-10-10"));
        course.setFinishDate(Date.valueOf("2020-12-10"));
        return course;
    }
}
