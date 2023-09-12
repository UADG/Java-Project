package cs211.project.models;

public class User extends Account{
    public User(String username, String password, String name, String time){
        super("user", username, password, name, time);
    }
}
