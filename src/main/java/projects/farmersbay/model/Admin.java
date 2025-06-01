package projects.farmersbay.model;

public class Admin {

    private int AdminID;
    private String username;
    private String password;

    public Admin(int AdminID, String username, String password) {
        this.AdminID = AdminID;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return AdminID;
    }

    public void setId(int AdminID) {
        this.AdminID = AdminID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
