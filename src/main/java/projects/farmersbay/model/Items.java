package projects.farmersbay.model;

public class Items {

    private int id;
    private String itemName;
    private double price;
    private int stock;
    private int AdminID;

    public Items() {}

    public  Items(int id, String itemName, double price, int stock, int AdminID) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.stock = stock;
        this.AdminID = AdminID;
    }

    public int getItemId() {
        return id;
    }

    public void setItemId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    @Override
    public String toString() {
        return "Items{"
                + "id=" + id
                + ", itemName='" + itemName + '\''
                + ", price=" + price
                + ", stock=" + stock
                + ", AdminID=" + AdminID
                + '}';
    }
}
