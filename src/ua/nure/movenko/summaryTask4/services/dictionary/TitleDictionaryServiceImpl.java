package ua.nure.movenko.summaryTask4.services.dictionary;

import ua.nure.movenko.summaryTask4.db.transaction.TransactionManager;
import ua.nure.movenko.summaryTask4.db.dao.dictionary.TitleDictionaryDao;
import ua.nure.movenko.summaryTask4.entities.TitleDictionary;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * TitleDictionaryServiceImpl based  on implementation of the {@code TitleDictionaryService} interface.
 *
 *  @ author M.Movenko
 */
public class TitleDictionaryServiceImpl implements TitleDictionaryService {

    private TransactionManager transactionManager;
    private TitleDictionaryDao titleDictionaryDao;

    public TitleDictionaryServiceImpl(TransactionManager transactionManager, TitleDictionaryDao titleDictionaryDao) {
        this.transactionManager = transactionManager;
        this.titleDictionaryDao = titleDictionaryDao;
    }

    @Override
    public boolean exists(String title) {
        return transactionManager.execute(connection -> titleDictionaryDao.exists(title, connection),
                Connection.TRANSACTION_NONE);
    }

    @Override
    public int add(TitleDictionary entity) {
        return transactionManager.execute(connection -> titleDictionaryDao.add(entity, connection),
                Connection.TRANSACTION_NONE);
    }

    @Override
    public boolean update(TitleDictionary entity) {
        return transactionManager.execute(connection -> titleDictionaryDao.update(entity, connection),
                Connection.TRANSACTION_NONE);
    }

    @Override
    public TitleDictionary getById(int id) {
        return transactionManager.execute(connection -> titleDictionaryDao.getById(id, connection),
                Connection.TRANSACTION_NONE);
    }

    @Override
    public TitleDictionary extract(ResultSet resultSet) {
        return transactionManager.execute(connection -> extract(resultSet),
                Connection.TRANSACTION_NONE);
    }

    @Override
    public boolean titleAlreadyInUsage(String title, int courseId) {
        return transactionManager.execute(connection ->
                titleDictionaryDao.titleAlreadyInUsage(title, courseId, connection), Connection.TRANSACTION_NONE);
    }

}
