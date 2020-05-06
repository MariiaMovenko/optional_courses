package ua.nure.movenko.summaryTask4.db.dao.dictionary;

import org.apache.log4j.Logger;
import ua.nure.movenko.summaryTask4.db.dao.AbstractOperationDAO;
import ua.nure.movenko.summaryTask4.constants.DBFields;
import ua.nure.movenko.summaryTask4.entities.TitleDictionary;
import ua.nure.movenko.summaryTask4.exception.OperationDaoException;

import java.sql.*;
import java.util.List;

/**
 * TitleDictionaryDAOImpl based  on implementation of the {@code TitleDictionaryDAO} interface.
 *
 *  @ author M.Movenko
 */
public class TitleDictionaryDAOImpl extends AbstractOperationDAO<TitleDictionary> implements TitleDictionaryDao {

    private static final Logger LOG = Logger.getLogger(TitleDictionaryDAOImpl.class);

    private static final String GET_ALL_BY_TITLE = "SELECT * FROM title_dictionary WHERE en=? or ru=?";
    private static final String GET_TITLE_BY_ID = "SELECT * FROM title_dictionary WHERE id=?";
    private static final String UPDATE_TITLE = "UPDATE title_dictionary SET  en=?, ru=? WHERE id=? ";
    private static final String ADD_TITLE_TRANSLATION = "INSERT INTO title_dictionary VALUES (DEFAULT, ?, ?)";
    private static final String FIND_THE_SAME_TITLE = "SELECT title_dictionary.* FROM title_dictionary INNER JOIN " +
            "courses ON  title_dictionary.id=courses.title_id WHERE (en=? OR ru=?) AND NOT courses.id=?";

    @Override
    public TitleDictionary getByTitle(String title, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_BY_TITLE)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, title);
            List<TitleDictionary> records = executeQuery(preparedStatement);
            return records.isEmpty() ? null : records.get(0);
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + GET_ALL_BY_TITLE + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + GET_ALL_BY_TITLE + "']");
        }
    }

    @Override
    public boolean titleAlreadyInUsage(String title, int courseId, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_THE_SAME_TITLE)) {
            int k = 1;
            preparedStatement.setString(k++, title);
            preparedStatement.setString(k++, title);
            preparedStatement.setInt(k, courseId);
            List<TitleDictionary> records = executeQuery(preparedStatement);
            return !records.isEmpty();
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + FIND_THE_SAME_TITLE + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + FIND_THE_SAME_TITLE + "']");
        }
    }

    @Override
    public boolean exists(String title, Connection connection) {
        return getByTitle(title, connection) != null;
    }

    @Override
    public int add(TitleDictionary entity, Connection connection) {

        try (PreparedStatement preparedStatement = connection
                .prepareStatement(ADD_TITLE_TRANSLATION, Statement.RETURN_GENERATED_KEYS)) {
            int k = 1;
            preparedStatement.setString(k++, entity.getEn());
            preparedStatement.setString(k, entity.getRu());
            if (preparedStatement.executeUpdate() > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    entity.setId(resultSet.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + ADD_TITLE_TRANSLATION + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + ADD_TITLE_TRANSLATION + "']", e);
        }
        return entity.getId();
    }

    @Override
    public boolean update(TitleDictionary entity, Connection connection) {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement(UPDATE_TITLE)) {
            int k = 1;
            preparedStatement.setString(k++, entity.getEn());
            preparedStatement.setString(k++, entity.getRu());
            preparedStatement.setInt(k, entity.getId());
            return preparedStatement.executeUpdate()> 0;
        } catch (SQLException e) {
            LOG.error("Can't handle sql ['" + UPDATE_TITLE + "']", e);
            throw new OperationDaoException("Can't handle sql ['" + UPDATE_TITLE + "']", e);
        }
    }

    @Override
    public boolean deleteById(int id, Connection connection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TitleDictionary getById(int id, Connection connection) {
        return getById(id, GET_TITLE_BY_ID, connection);
    }

    @Override
    public List<TitleDictionary> getAll(Connection connection, Integer limit, Integer offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TitleDictionary> getAll(Connection connection) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected TitleDictionary extract(ResultSet resultSet) throws SQLException {
        TitleDictionary titleDictionary = new TitleDictionary();
        titleDictionary.setEn(resultSet.getString(DBFields.TITLE_EN));
        titleDictionary.setRu(resultSet.getString(DBFields.TITLE_RU));
        titleDictionary.setId(resultSet.getInt(DBFields.ENTITY_ID));
        return titleDictionary;
    }
}
