package cs211.project.models;

import java.util.ArrayList;

public class Account {
    protected String username;
    protected String name;
    protected String password;
    protected String role;
    protected String time;
    protected String pictureURL;
    protected String userStatus;
    protected ArrayList<String> allEventUser;

    protected final int id;

    public Account(int id, String username, String password, String name, String time, String pictureURL, String userStatus, String role) {
        this.id = id;
        this.role = role;
        this.username = username;
        this.name = name;
        this.password = password;
        this.time = time;
        this.pictureURL = pictureURL;
        this.userStatus = userStatus;
        allEventUser = new ArrayList<>();
    }

    public void addUserEventName(String name) {
        allEventUser.add(name);
    }

    public void deleteUserEventName(String name) {
        if (allEventUser.contains(name)) {
            allEventUser.remove(name);
        }
    }

    public String getEventName(String name) {
        for (String event : allEventUser) {
            if (event.equals(name)) {
                return event;
            }
        }
        return null;
    }

    public ArrayList<String> getAllEventUser() {
        return allEventUser;
    }

    public boolean isEventName(String eventName) {
        for (String event : this.allEventUser) {
            if (event.equals(eventName)) {
                return true;
            }
        }
        return false;
    }

    public String getRole() { return role; }
    public String getUsername() {
        return username;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public String getTime() {
        return time;
    }
    public String getPictureURL() {
        return pictureURL;
    }
    public String getUserStatus() { return userStatus; }
    public int getId(){return id;}
    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public boolean isUsername(String username) {
        return this.username.equals(username);
    }
    public boolean isPassword(String password) {
        return this.password.equals(password);
    }
    public boolean isAdmin(String role) {
        return "admin".equals(role);
    }
    public boolean isUnban(String userStatus) {
        return "unban".equals(userStatus);
    }
    @Override
    public String toString() {
        return "Role: " + role + " Username: " + username + " Last Online: " + time + " Status: " + userStatus;
    }
}
