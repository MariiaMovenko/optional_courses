package ua.nure.movenko.summaryTask4.db.dao.theme;


import ua.nure.movenko.summaryTask4.db.dao.OperationDAO;
import ua.nure.movenko.summaryTask4.entities.Theme;

import java.sql.Connection;

public interface ThemeDAO extends OperationDAO<Theme> {
   /**
    *Finds an object with the specified either Russian or English subject title version
    * @param title is criteria to find the object in the Database
    * @param connection to database
    * @return {@code null} if there no elements match criteria {@param title}
    */
   Theme getByTitle(String title, Connection connection);

   /**
    *Checks if an object with the specified {@param title} exists in the Database
    * @param title is either Russian or English  translation of theme's title
    * @param connection to Database
    * @return {@code true} if the specified {@param title} exists in the Database
    */
   boolean exists(String title, Connection connection);
}

