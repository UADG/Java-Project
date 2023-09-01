package cs211.project.models;

public class Admin extends Account {

    public Admin(int id, String username, String password, String name) {
        super(id, username, name, password);
    }

    public void changePassword(String password) {
        if (!password.trim().isEmpty()) {
            this.password = password.trim();
        }
    }

    @Override
    public String toString() {
        return  "ID: '" + id + "', " +
                "Username: '" + username + "', " +
                "Name: '" + name + "'";
    }

}
