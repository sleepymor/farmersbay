package projects.farmersbay.model;

public class Admin {

    private Integer AdminID;
    private String username;
    private String password;

    public Admin(Integer AdminID, String username, String password) {
        this.AdminID = AdminID;
        this.username = username;
        this.password = password;
    }

    public Admin() {
    }

    public Integer getId() {
        return AdminID;
    }

    public void setId(Integer AdminID) {
        this.AdminID = AdminID;
    }

    public String getName() {
        return username;
    }

    public void setName(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
