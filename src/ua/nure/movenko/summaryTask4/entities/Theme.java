package ua.nure.movenko.summaryTask4.entities;

/**
 * Theme entity.
 *
 * @author M.Movenko
 */
public class Theme extends Entity {

    private String themeTitleEn;
    private String themeTitleRu;

    public Theme() {
    }

    public Theme(String themeTitleEn, String themeTitleRu) {
        this.themeTitleEn = themeTitleEn;
        this.themeTitleRu = themeTitleRu;
    }

    public String getThemeTitleEn() {
        return themeTitleEn;
    }

    public void setThemeTitleEn(String themeTitleEn) {
        this.themeTitleEn = themeTitleEn;
    }

    public String getThemeTitleRu() {
        return themeTitleRu;
    }

    public void setThemeTitleRu(String themeTitleRu) {
        this.themeTitleRu = themeTitleRu;
    }

    @Override
    public String toString() {
        return "Theme{" +
                "themeTitleEn='" + themeTitleEn + '\'' +
                ", themeTitleRu='" + themeTitleRu + '\'' +
                '}';
    }
}
