package ua.nure.movenko.summaryTask4.db.dao.theme;

import org.apache.log4j.Logger;
import ua.nure.movenko.summaryTask4.db.dao.AbstractOperationDAO;
import ua.nure.movenko.summaryTask4.constants.DBFields;
import ua.nure.movenko.summaryTask4.entities.Theme;
import ua.nure.movenko.summaryTask4.exception.OperationDaoException;

import java.sql.*;
import java.util.List;

/**
 * ThemeDAOImpl based  on implementation of the {@code ThemeDAO} interface.
 *
 *  @ author M.Movenko
 */
public class ThemeDAOImpl extends AbstractOperationDAO<Theme> implements ThemeDAO {

    private static final Logger LOG = Logger.getLogger(ThemeDAOImpl.class);

    private static final String GET_THEME_BY_TITLE = "SELECT * FROM themes WHERE en_theme=? or ru_theme=?";
    private static final String GET_THEME_BY_ID = "SELECT * FROM themes WHERE id=?";
    private static final String GET_ALL_THEMES = "SELECT * FROM themes";
    private static final String GET_ALL_THEMES_LIMITED = "SELECT * FROM themes LIMIT ? OFFSET ? ";
    private static final String UPDATE_THEME = "UPDATE themes SET  en_theme=?, ru_theme=? WHERE id=? ";
    private static final String ADD_THEME = "INSERT INTO themes VALUES (DEFAULT, ?, ?)";

    @Override
    protected Theme extract(ResultSet resultSet) throws SQLException {
        Theme theme = new Theme();
        theme.setThemeTitleEn(resultSet.getString(DBFields.THEME_EN));
        theme.setThemeTitleRu(resultSet.getString(DBFields.THEME_RU));
        theme.setId(resultSet.getInt(DBFields.ENTITY_ID));
        return theme;
    }

    @Override
    public int add(Theme entity, Connection connection) {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement(ADD_THEME, Statement.RETURN_GENERATED_KEYS)) {
            int k = 1;
            preparedStatement.setString(k++, entity.getThemeTitleEn());
            preparedStatement.setString(k, entity.getThemeTitleRu());
            if (preparedStatement.executeUpdate() > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    entity.setId(resultSet.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + ADD_THEME + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + ADD_THEME + "']", e);
        }
        return entity.getId();
    }

    @Override
    public boolean update(Theme entity, Connection connection) {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement(UPDATE_THEME)) {
            int k = 1;
            preparedStatement.setString(k++, entity.getThemeTitleEn());
            preparedStatement.setString(k++, entity.getThemeTitleRu());
            preparedStatement.setInt(k, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + UPDATE_THEME + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + UPDATE_THEME + "']", e);
        }
        return true;
    }


    @Override
    public boolean deleteById(int id, Connection connection) {
        throw new UnsupportedOperationException();

    }

    @Override
    public Theme getById(int id, Connection connection) {
        return getById(id, GET_THEME_BY_ID, connection);
    }

    @Override
    public List<Theme> getAll(Connection connection, Integer limit, Integer offset) {
        return getAll(GET_ALL_THEMES_LIMITED, connection, limit, offset);
    }

    @Override
    public List<Theme> getAll(Connection connection) {
        return getAll(GET_ALL_THEMES, connection);
    }

    @Override
    public Theme getByTitle(String title, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_THEME_BY_TITLE)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, title);
            List<Theme> records = executeQuery(preparedStatement);
            return records.isEmpty() ? null : records.get(0);
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + GET_THEME_BY_TITLE + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + GET_THEME_BY_TITLE + "']");
        }
    }

    @Override
    public boolean exists(String title, Connection connection) {
        return getByTitle(title, connection) != null;
    }
}
