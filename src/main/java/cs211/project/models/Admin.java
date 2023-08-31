package cs211.project.models;

public class Admin extends Account{
    private int id;
    private String username;
    private String password;
    private String name;

    public Admin(int id, String username, String password, String name) {
        super(id, username, name, password);
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
