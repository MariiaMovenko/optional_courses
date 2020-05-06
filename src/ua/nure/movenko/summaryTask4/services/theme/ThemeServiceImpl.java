package ua.nure.movenko.summaryTask4.services.theme;

import ua.nure.movenko.summaryTask4.db.transaction.TransactionManager;
import ua.nure.movenko.summaryTask4.entities.Theme;
import ua.nure.movenko.summaryTask4.db.dao.theme.ThemeDAO;
import ua.nure.movenko.summaryTask4.models.ThemeModel;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ThemeServiceImpl based  on implementation of the {@code ThemeService} interface.
 *
 *  @ author M.Movenko
 */
public class ThemeServiceImpl implements ThemeService {

    TransactionManager transactionManager;
    ThemeDAO themeDAO;

    public ThemeServiceImpl(TransactionManager transactionManager, ThemeDAO themeDAO) {
        this.transactionManager = transactionManager;
        this.themeDAO = themeDAO;
    }

    @Override
    public List<ThemeModel> getAllThemes(String language) {
        return transactionManager.execute(connection -> themeDAO.getAll(connection)
                .stream().map(theme -> convertToModel(theme, language))
                .collect(Collectors.toList()), Connection.TRANSACTION_NONE);
    }

    @Override
    public boolean exists(String title) {
        return transactionManager
                .execute(connection -> themeDAO.exists(title, connection), Connection.TRANSACTION_NONE);
    }

    @Override
    public void addTheme(Theme theme) {
        transactionManager
                .execute(connection -> themeDAO.add(theme, connection), Connection.TRANSACTION_NONE);
    }

    @Override
    public boolean updateTheme(Theme theme) {
        return transactionManager
                .execute(connection -> themeDAO.update(theme, connection), Connection.TRANSACTION_NONE);
    }

    private ThemeModel convertToModel(Theme theme, String language) {
        ThemeModel model = new ThemeModel();
        if ("en".equals(language)) {
            model.setThemeTitle(theme.getThemeTitleEn());
        } else {
            model.setThemeTitle(theme.getThemeTitleRu());
        }
        return model;
    }
}
