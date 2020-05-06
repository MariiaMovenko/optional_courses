package ua.nure.movenko.summaryTask4.models;

/**
 * DropdownListItemModel entity
 *
 * @author M.Movenko
 */
public class DropdownListItemModel {

    private String itemTitle;

    public DropdownListItemModel(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

}
