package ua.nure.movenko.summaryTask4.services.dictionary;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.nure.movenko.summaryTask4.db.dao.dictionary.TitleDictionaryDAOImpl;
import ua.nure.movenko.summaryTask4.db.dao.dictionary.TitleDictionaryDao;
import ua.nure.movenko.summaryTask4.db.transaction.TransactionManagerMock;
import ua.nure.movenko.summaryTask4.entities.TitleDictionary;


import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DictionaryServiceImplTest {

    private TitleDictionary dictionaryTemplate;
    @Mock
    private final TitleDictionaryDao dictionaryDao = new TitleDictionaryDAOImpl();

    private final TransactionManagerMock transactionManager = new TransactionManagerMock();

    @InjectMocks
    private final TitleDictionaryService dictionaryService = new TitleDictionaryServiceImpl(transactionManager, dictionaryDao);

    @Before
    public void setUp() {
        dictionaryTemplate = new TitleDictionary();
        dictionaryTemplate.setEn("Title");
        dictionaryTemplate.setRu("Название");
        dictionaryTemplate.setId(4);
    }

    @Test
    public void shouldCheckIfExists() throws Exception {
        String title = dictionaryTemplate.getRu();
        when(dictionaryDao.exists(title, transactionManager.getConnectionForTest())).thenReturn(false);

        boolean exists = dictionaryService.exists(title);
        Assert.assertFalse(exists);
        transactionManager.verifyExecutedWithoutTransaction();
    }

    @Test
    public void shouldAdd() throws Exception {
        Integer id = 4;
        when(dictionaryDao.add(dictionaryTemplate, transactionManager.getConnectionForTest())).thenReturn(id);

        Integer result = dictionaryService.add(dictionaryTemplate);

        Assert.assertEquals(result, id);
        transactionManager.verifyExecutedWithoutTransaction();
    }

    @Test
    public void shouldUpdate() throws Exception {
        when(dictionaryDao.update(dictionaryTemplate, transactionManager.getConnectionForTest())).thenReturn(true);

        Assert.assertTrue(dictionaryService.update(dictionaryTemplate));
        transactionManager.verifyExecutedWithoutTransaction();
    }

    @Test
    public void IfAlreadyExists() throws Exception {
        String title = "Test";
        int courseId = 3;
        when(dictionaryDao.titleAlreadyInUsage(title, courseId, transactionManager.getConnectionForTest()))
                .thenReturn(false);

        Assert.assertFalse(dictionaryService.titleAlreadyInUsage(title, courseId));
        transactionManager.verifyExecutedWithoutTransaction();
    }
}

