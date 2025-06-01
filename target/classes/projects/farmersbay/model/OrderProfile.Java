package projects.farmersbay.model;

public class OrderProfile {
    private int id;
    private int userID;
    private String orderStatus;
    private String orderDate;

    public OrderProfile(int id, int userID, String orderStatus, String orderDate) {
        this.id = id;
        this.userID = userID;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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