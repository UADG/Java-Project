package cs211.project.models;

public class Admin extends User{
    private int id;
    private String username;
    private String password;
    private String name;

    public Admin(String username, String password, String name) {
        super(username, name, password);
    }

    public void changePassword(String password) {
        if (!password.trim().isEmpty()) {
            this.password = password.trim();
        }
    }

    public boolean isId(int id) {
        return this.id == id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return  "ID: '" + id + "', " +
                "Username: '" + username + "', " +
                "Name: '" + name + "'";
    }

}
