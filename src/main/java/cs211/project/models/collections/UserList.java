package cs211.project.models.collections;
import cs211.project.models.User;

import java.util.ArrayList;
public class UserList {
    private ArrayList<User> users;

    public UserList() {
        users = new ArrayList<>();
    }

    public void addNewUser(String username, String password, String name, String time, String picURL) {
        username = username.trim();
        name = name.trim();
        if (!username.equals("") && !name.equals("")) {
            User exist = findUserByUsername(username);
            if (exist == null) {
                users.add(new User(username, name, password.trim(), time.trim(), picURL.trim()));
            }
        }
    }

    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.isUsername(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean findUserPassword (String password){
        for (User user : users) {
            if (user.isPassword(password)) {
                return true;
            }
        }
        return false;
    }

    public String checkRole ( int id){
        if (id > 10) {
            return "user";
        } else {
            return "admin";
        }
    }
    public ArrayList<User> getUsers () {
        return users;
    }
}
