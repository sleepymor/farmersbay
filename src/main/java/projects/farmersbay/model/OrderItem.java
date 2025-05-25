package projects.farmersbay.model;

public class OrderItem {

    private int id;
    private int orderID;
    private int itemID;
    private int quantity;

    public OrderItem(int id, int orderID, int itemID, int quantity) {
        this.id = id;
        this.orderID = orderID;
        this.itemID = itemID;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderItem{"
                + "id=" + id
                + ", orderID=" + orderID
                + ", itemID=" + itemID
                + ", quantity=" + quantity
                + '}';
    }
}
