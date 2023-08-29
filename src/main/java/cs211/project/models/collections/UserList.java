package cs211.project.models.collections;
import cs211.project.models.User;

import java.util.ArrayList;
public class UserList{
    private ArrayList<User> users;
    public UserList() {
        users = new ArrayList<>();
    }
    public void addNewUser(String username, String password, String name, String time) {
        username = username.trim();
        name = name.trim();
        password = password.trim();
        time = time.trim();
        if (!username.equals("")&&!name.equals("")) {
            User exist = findUserByUsername(username);
            if (exist == null) {
                users.add(new User(username.trim(), name.trim(), password.trim(), time.trim()));
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
    public ArrayList<User> getUsers(){
        return users;
    }
}
