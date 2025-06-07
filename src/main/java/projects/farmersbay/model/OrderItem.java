package projects.farmersbay.model;

public class OrderItem {

    private int orderID;
    private int itemID;
    private int Quantity;

    public OrderItem(int orderID, int itemID, int Quantity) {
        this.orderID = orderID;
        this.itemID = itemID;
        this.Quantity = Quantity;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }
}
