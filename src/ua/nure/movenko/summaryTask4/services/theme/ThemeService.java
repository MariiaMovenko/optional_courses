package ua.nure.movenko.summaryTask4.services.theme;

import ua.nure.movenko.summaryTask4.entities.Theme;
import ua.nure.movenko.summaryTask4.models.ThemeModel;

import java.util.List;

public interface ThemeService {
    /**
     * Method adds  new {@code theme} object to the system
     *
     * @param theme an object to  add
     * @return generated  for added theme identical code {@code int}
     */
    void addTheme(Theme theme);

    /**
     * Method updates  {@code theme} parameters in the system
     *
     * @return {@code true} if at least one parameter was updated
     */
    boolean updateTheme(Theme theme);

    /**
     * Finds and returns theme models' list that represents themes adapted to view layer
     *
     * @param language defines view layer language information on returned themes to be translated to
     * @return Returns list of models
     */
    List<ThemeModel> getAllThemes(String language);

    /**
     * Check if  theme with the particular {@param title} exists
     *
     * @param title of chosen theme
     * @return {@code true} if the theme with the specified title exists
     */
    boolean exists(String title);

}
