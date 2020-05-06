package ua.nure.movenko.summaryTask4.entities;

/**
 * TitleDictionary entity.
 *
 * @author M.Movenko
 */
public class TitleDictionary extends Entity {
    private String en;
    private String ru;

    public TitleDictionary() {
    }

    public TitleDictionary(String en, String ru) {
        this.en = en;
        this.ru = ru;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getRu() {
        return ru;
    }

    public void setRu(String ru) {
        this.ru = ru;
    }

    @Override
    public String toString() {
        return "TitleDictionary{" +
                "en='" + en + '\'' +
                ", ru='" + ru + '\'' +
                '}';
    }
}
