package cs211.project.models;
import java.util.ArrayList;
public class UserList{
    private ArrayList<User> users;
    public UserList() {
        users = new ArrayList<>();
    }
    public void addNewUser(int id, String username, String password, String name) {
        username = username.trim();
        name = name.trim();
        password = password.trim();
        if (!username.equals("")) {
            User exist = findUserByUsername(username);
            if (exist == null) {
                users.add(new User(username.trim(), name.trim(), password.trim()));
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
