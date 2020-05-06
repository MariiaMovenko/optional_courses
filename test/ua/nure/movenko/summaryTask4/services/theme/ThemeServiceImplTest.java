package ua.nure.movenko.summaryTask4.services.theme;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.nure.movenko.summaryTask4.db.dao.theme.ThemeDAO;
import ua.nure.movenko.summaryTask4.db.transaction.TransactionManagerMock;
import ua.nure.movenko.summaryTask4.entities.Theme;
import ua.nure.movenko.summaryTask4.models.ThemeModel;

import java.util.List;

import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ThemeServiceImplTest {

    @Mock
    private ThemeDAO themeDAO;

    private final TransactionManagerMock transactionManager = new TransactionManagerMock();

    @InjectMocks
    private final ThemeService themeService = new ThemeServiceImpl(transactionManager, themeDAO);

    @Test
    public void shouldGetAllThemes() throws Exception {
        String language = "ru";
        Theme theme1 = new Theme("Theme1", "Тема1");
        theme1.setId(1);
        Theme theme2 = new Theme("Theme2", "Тема2");
        theme2.setId(2);
        List<Theme> themes = List.of(theme1, theme2);
        when(themeDAO.getAll(transactionManager.getConnectionForTest())).thenReturn(themes);

        List<ThemeModel> result = themeService.getAllThemes(language);

        Assert.assertEquals(result.size(), 2);
        Assert.assertEquals(result.get(0).getThemeTitle(), "Тема1");
        Assert.assertEquals(result.get(1).getThemeTitle(), "Тема2");
        transactionManager.verifyExecutedWithoutTransaction();
    }

    @Test
    public void IfExists() throws Exception {
        String title = "NotExist";
        when(themeDAO.exists(title, transactionManager.getConnectionForTest())).thenReturn(false);

        boolean exists = themeService.exists(title);

        Assert.assertFalse(exists);
        transactionManager.verifyExecutedWithoutTransaction();
    }

    @Test
    public void shouldUpdateTheme() throws Exception {
        Theme upToDateTheme = new Theme("NewTheme", "Новая Тема");
        upToDateTheme.setId(27);
        when(themeDAO.update(upToDateTheme, transactionManager.getConnectionForTest())).thenReturn(true);

        boolean updated = themeService.updateTheme(upToDateTheme);
        Assert.assertTrue(updated);
        transactionManager.verifyExecutedWithoutTransaction();
    }




}
