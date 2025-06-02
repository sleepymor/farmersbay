package projects.farmersbay.model;

public class Items {

    private int ItemsID;
    private String title;
    private double price;
    private int stock;
    private int AdminID;

    public Items() {}

    public  Items(int ItemsID, String title, double price, int stock, int AdminID) {
        this.ItemsID = ItemsID;
        this.title = title;
        this.price = price;
        this.stock = stock;
        this.AdminID = AdminID;
    }

    public int getItemId() {
        return ItemsID;
    }

    public void setItemId(int ItemsID) {
        this.ItemsID = ItemsID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getAdminId() {
        return AdminID;
    }

    public void setAdminId(int AdminID) {
        this.AdminID = AdminID;
    }
}
