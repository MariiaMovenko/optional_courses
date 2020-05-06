package ua.nure.movenko.summaryTask4.db.dao.theme;

import com.mysql.jdbc.Driver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ua.nure.movenko.summaryTask4.entities.Theme;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class ThemeDAOImplTest {
    private final ThemeDAO themeDao = new ThemeDAOImpl();
    Connection connection;

    @Before
    public void setUp() {
        connection = getConnection();
    }

    @After
    public void tearDown() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE  FROM themes");
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            System.err.println("Can't close connection.\n");
            e.printStackTrace();
        }
    }

    @Test
    public void ShouldAddTheme() {
        Theme maths = new Theme("Maths", "Математика");
        Theme biology = new Theme("Biology", "Биология");

        int mathsId = themeDao.add(maths, connection);
        int biologyId = themeDao.add(biology, connection);
        List<Theme> allThemes = themeDao.getAll(connection);
        Assert.assertTrue(mathsId > 0);
        Assert.assertTrue(biologyId > 0);
        Assert.assertEquals(2, allThemes.size());
    }

    @Test
    public void ShouldUpdateTheme() {
        Theme testTheme = new Theme("Maths", "Математика");
        int id = themeDao.add(testTheme, connection);
        Theme newTheme = new Theme("Biology", "Биология");
        newTheme.setId(testTheme.getId());

        boolean updated = themeDao.update(newTheme, connection);
        Theme afterUpdate = themeDao.getById(id, connection);
        Assert.assertTrue(updated);
        Assert.assertEquals(testTheme.getId(), afterUpdate.getId());
        Assert.assertNotEquals(testTheme.getThemeTitleEn(), afterUpdate.getThemeTitleEn());
    }


    @Test
    public void ShouldGetAllThemes() {
        Theme maths = new Theme("Maths", "Математика");
        Theme biology = new Theme("Biology", "Биология");
        int mathsId = themeDao.add(maths, connection);
        int biologyId = themeDao.add(biology, connection);

        List<Theme> allThemes = themeDao.getAll(connection);
        Assert.assertEquals(2, allThemes.size());
        Assert.assertEquals("Maths", allThemes.get(0).getThemeTitleEn());
        Assert.assertEquals("Biology", allThemes.get(1).getThemeTitleEn());
    }

    @Test
    public void ShouldGetThemeByTitle() {
        Theme maths = new Theme("Maths", "Математика");
        Theme biology = new Theme("Biology", "Биология");
        themeDao.add(maths, connection);
        themeDao.add(biology, connection);

        Theme result = themeDao.getByTitle("Maths", connection);
        Assert.assertNotNull(result);
        Assert.assertEquals(maths.getId(), result.getId());
    }

    @Test
    public void ShouldGetThemeById() {
        Theme maths = new Theme("Maths", "Математика");
        int mathsId = themeDao.add(maths, connection);

        Theme result = themeDao.getById(mathsId, connection);
        Assert.assertNotNull(result);
        Assert.assertEquals(maths.getId(), result.getId());
        Assert.assertEquals("Maths", result.getThemeTitleEn());
    }

    @Test
    public void testGetAll() {
        Theme maths = new Theme("Maths", "Математика");
        Theme biology = new Theme("Biology", "Биология");
        themeDao.add(maths, connection);
        themeDao.add(biology, connection);

        List<Theme> result = themeDao.getAll(connection);
        Assert.assertEquals(2, result.size());
    }

    @Test
    public void testGetAllLimited() {
        Theme maths = new Theme("Maths", "Математика");
        Theme biology = new Theme("Biology", "Биология");
        themeDao.add(maths, connection);
        themeDao.add(biology, connection);
        int limit = 2;
        int offset = 1;

        List<Theme> result = themeDao.getAll(connection, limit, offset);
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void testExistsMethod() {
        Theme maths = new Theme("Maths", "Математика");
        int mathsId = themeDao.add(maths, connection);

        boolean exists = themeDao.exists("Maths", connection);
        Assert.assertTrue(exists);
    }

    @Test
    public void testDeleteByIdThrowsException() {
        Theme maths = new Theme("Maths", "Математика");
        int mathsId = themeDao.add(maths, connection);

        Assert.assertThrows(UnsupportedOperationException.class,
                () -> themeDao.deleteById(mathsId, connection));
    }

    public static Connection getConnection() {
        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "admin");

        Connection connection = null;
        try {
            connection = ((Driver) Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance())
                    .connect("jdbc:mysql://localhost:3306/test_db?serverTimezone=UTC", props);
        } catch (ClassNotFoundException e) {
            System.err.println("Where is your JDBC Driver?\n");
            e.printStackTrace();
            System.exit(1);
        } catch (SQLException e) {
            System.err.println("Connection Failed.\n");
            e.printStackTrace();
            System.exit(1);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return connection;
    }

}
