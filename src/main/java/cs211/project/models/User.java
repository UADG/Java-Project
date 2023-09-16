package cs211.project.models;

import java.util.ArrayList;

public class User extends Account{
    ArrayList<String> eventName;

    public User(String username, String password, String name, String time, String picURL){
        super("user", username, password, name, time, picURL);
        eventName = new ArrayList<>();
    }

    public void addEventName(String name) {
        eventName.add(name);
    }

    public void deleteEventName(String name) {
        eventName.add(name);
    }

}