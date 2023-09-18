package cs211.project.models;

import java.util.ArrayList;

public class User extends Account{

    public User(String username, String password, String name, String time, String picURL){
        super("user", username, password, name, time, picURL);
    }

}