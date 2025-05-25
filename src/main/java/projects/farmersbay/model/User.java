package projects.farmersbay.model;

public class User {
    private Integer UserID;
    private String name;
    private String password;

    public User() {} // required for read from DB

    public User(Integer UserID, String name, String password) {
        this.UserID = UserID;
        this.name = name;
        this.password = password;
    }

    public Integer getId() {
        return UserID;
    }

    public void setId(Integer UserID) { this.UserID = UserID; }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { this.password = password; }
}
