package cs211.project.models;

import java.util.ArrayList;

public class User extends Account {
    ArrayList<String> eventName;

    public User(String username, String name, String password, String time) {
        super(username, name, password, time);
        eventName = new ArrayList<>();
    }

    public void addEventName(String name) {
        eventName.add(name);
    }

    public void deleteEventName(String name) {
        eventName.add(name);
    }

}