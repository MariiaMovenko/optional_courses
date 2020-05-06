package ua.nure.movenko.summaryTask4.db.dao.dictionary;

import com.mysql.jdbc.Driver;
import org.junit.*;
import ua.nure.movenko.summaryTask4.entities.TitleDictionary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class TitleDictionaryDAOImplTest {

    private final TitleDictionaryDao dictionaryDao = new TitleDictionaryDAOImpl();
    Connection connection;

    @Before
    public void setUp() {
        connection = getConnection();
    }

    @Test
    public void shouldGetByTitle() {
        TitleDictionary dictionary = new TitleDictionary("Title1", "Название");
        dictionaryDao.add(dictionary, connection);

        TitleDictionary result = dictionaryDao.getByTitle("Название", connection);
        Assert.assertNotNull(result);
        Assert.assertEquals(dictionary.getId(), result.getId());
    }

    @Test
    public void shouldGetById() {
        TitleDictionary dictionary = new TitleDictionary("Title1", "Название");
        int id = dictionaryDao.add(dictionary, connection);

        TitleDictionary result = dictionaryDao.getById(id, connection);
        Assert.assertNotNull(result);
        Assert.assertEquals("Title1", result.getEn());
    }

    @Test
    public void shouldUpdateTitle() {
        TitleDictionary dictionary = new TitleDictionary("Title1", "Название");
        int id = dictionaryDao.add(dictionary, connection);

        dictionary.setEn("New Title");
        dictionary.setRu("New Title Russian");

        boolean updated = dictionaryDao.update(dictionary, connection);
        TitleDictionary afterUpdate = dictionaryDao.getById(id, connection);
        Assert.assertTrue(updated);
        Assert.assertEquals("New Title", afterUpdate.getEn());
        Assert.assertEquals(dictionary.getId(), afterUpdate.getId());
    }

    @Test
    public void shouldAddNewTitle() {
        TitleDictionary dictionary = new TitleDictionary("Title", "Название");

        int id = dictionaryDao.add(dictionary, connection);
        TitleDictionary testIfAdded = dictionaryDao.getById(id, connection);
        Assert.assertTrue(id > 0);
        Assert.assertNotNull(testIfAdded);
        Assert.assertEquals("Title", testIfAdded.getEn());
        Assert.assertEquals("Название", testIfAdded.getRu());
    }

    @Test
    public void testIfTitleAlreadyInUsage() {
        TitleDictionary dictionary = new TitleDictionary("Title", "Название");
        int thisTitleId = dictionaryDao.add(dictionary, connection);

        boolean result1 = dictionaryDao.titleAlreadyInUsage("Title", thisTitleId,connection);
        boolean result2 = dictionaryDao.titleAlreadyInUsage("Title", thisTitleId+33, connection);
        Assert.assertFalse(result1);
        Assert.assertFalse(result2);
    }

    @Test
    public void testDeleteByIdThrowsException() {
        TitleDictionary dictionary = new TitleDictionary("Title", "Название");
        int id = dictionaryDao.add(dictionary, connection);

        Assert.assertThrows(UnsupportedOperationException.class,
                () -> dictionaryDao.deleteById(id, connection));
    }

    @Test
    public void testGetAllThrowsException() {
        Assert.assertThrows(UnsupportedOperationException.class,
                () -> dictionaryDao.getAll(connection));
    }

    @After
    public void tearDown() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE  FROM title_dictionary");
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            System.err.println("Can't close connection.\n");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "admin");

        Connection connection = null;
        try {
            connection = ((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance())
                    .connect("jdbc:mysql://localhost:3306/test_db?serverTimezone=UTC", props);
        } catch (ClassNotFoundException e) {
            System.err.println("Where is your JDBC Driver?\n");
            e.printStackTrace();
            System.exit(1);
        } catch (SQLException e) {
            System.err.println("Connection Failed.\n");
            e.printStackTrace();
            System.exit(1);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        return connection;
    }
}
