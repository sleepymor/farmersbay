package projects.farmersbay.model;

public class Category {

    private int CategoryID;
    private String Title;

    public Category() {}

    public Category(int CategoryID, String Title) {
        this.CategoryID = CategoryID;
        this.Title = Title;
    }

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int CategoryID) {
        this.CategoryID = CategoryID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }
}