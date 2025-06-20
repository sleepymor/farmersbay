package projects.farmersbay.model;

public class OrderProfile {
    private int OrderID;
    private int userID;
    private String orderStatus;
    private String orderDate;

    public OrderProfile(int OrderID, int userID, String orderStatus, String orderDate) {
        this.OrderID = OrderID;
        this.userID = userID;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
    }

    public int getId() {
        return OrderID;
    }

    public void setId(int OrderID) {
        this.OrderID = OrderID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}