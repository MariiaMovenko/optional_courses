package ua.nure.movenko.summaryTask4.services.journal;

import ua.nure.movenko.summaryTask4.db.transaction.TransactionManager;
import ua.nure.movenko.summaryTask4.db.dao.journal.JournalDAO;
import ua.nure.movenko.summaryTask4.bean.Journal;
import ua.nure.movenko.summaryTask4.entities.User;

import java.sql.Connection;
import java.util.List;

/**
 * JournalServiceImpl based  on implementation of the {@code JournalService} interface.
 *
 * @ author M.Movenko
 */
public class JournalServiceImpl implements JournalService {

    TransactionManager transactionManager;
    JournalDAO journalDAO;

    public JournalServiceImpl(TransactionManager transactionManager, JournalDAO journalDAO) {
        this.transactionManager = transactionManager;
        this.journalDAO = journalDAO;
    }

    @Override
    public boolean enrollStudent(User student, String courseTitle) {
        return transactionManager.execute(connection -> journalDAO
                .enrollStudent(student, connection, courseTitle), Connection.TRANSACTION_REPEATABLE_READ);
    }

    @Override
    public Journal getCourseJournal(User lector, Integer courseId) {
        return transactionManager.execute(connection -> journalDAO
                .getCourseJournal(connection, lector, courseId), Connection.TRANSACTION_NONE);
    }

    @Override
    public void updateJournal(List<Integer> studentId, List<String> mark, Integer courseId) {
        transactionManager.execute(connection -> journalDAO
                .updateJournal(studentId, mark, courseId, connection), Connection.TRANSACTION_REPEATABLE_READ);
    }
}
