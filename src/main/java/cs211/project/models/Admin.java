package cs211.project.models;

public class Admin extends Account {

    public Admin(String username, String password, String name, String time, String picURL) {
        super("admin", username, password, name, time, picURL, "unban");
    }

    public void changePassword(String password) {
        if (!password.trim().isEmpty()) {
            this.password = password.trim();
        }
    }

    @Override
    public String toString() {
        return  "Role: '" + role + "', " +
                "Username: '" + username + "', " +
                "Name: '" + name + "'";
    }

}
