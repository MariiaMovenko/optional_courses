package ua.nure.movenko.summaryTask4.db.dao.dictionary;

import ua.nure.movenko.summaryTask4.db.dao.OperationDAO;
import ua.nure.movenko.summaryTask4.entities.TitleDictionary;

import java.sql.Connection;

public interface TitleDictionaryDao extends OperationDAO<TitleDictionary> {

    /**
     * Returns the first (and the only occurrence because in the Database field title is unique)
     * occurrence of  TitleDictionary element by either English or Russian version of the  course title
     *
     * @param title      defines  either  English or Russian version of course title
     * @param connection to Database
     * @return TitleDictionary object which represents both Russian and English versions of course title
     */
    TitleDictionary getByTitle(String title, Connection connection);

    /**
     * Checks if the element with  the specified title exists;
     *
     * @param title      is either English or Russian version of the course title
     * @param connection to Database
     * @return {@code true} if the course with particular title exists
     */
    boolean exists(String title, Connection connection);

    /**
     * Checks if the particular title is already defined as title of the course except the  course with
     * {@param courseId}
     *
     * @param title      is either English or Russian version of the course title
     * @param courseId   defines course  excluded from revise
     * @param connection to Database
     * @return {@code true} if the particular title uses as title of some course except course with defined courseId
     */
    boolean titleAlreadyInUsage(String title, int courseId, Connection connection);

}
