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
    protected ArrayList<String> eventName;

    public Account(String role, String username, String password, String name, String time, String pictureURL) {
        this.role = role;
        this.username = username;
        this.name = name;
        this.password = password;
        this.time = time;
        this.pictureURL = getClass().getResource("/images/default-profile.png").toExternalForm();
        System.out.println(pictureURL);
        this.userStatus = "unban";
        eventName = new ArrayList<>();
    }

    public void addUserEventName(String name) {
        System.out.println(eventName + " name");
        eventName.add(name);
    }

    public void deleteUserEventName(String name) {
        if (eventName.contains(name)) {
            eventName.remove(name);
        }
    }

    public ArrayList<String> getEventName() {
        System.out.println(eventName + " event");
        return eventName;
    }

    public boolean isEventName(String eventName) {
        for (String event : this.eventName) {
            if (event.equals(eventName)) {
                return true;
            }
        }
        return false;
    }

    public String getRole(){return role;}
    public String getUsername(){
        return username;
    }
    public String getName(){
        return name;
    }
    public String getPassword() {
        return password;
    }
    public String getTime(){
        return time;
    }
    public String getPictureURL(){
        return pictureURL;
    }
    public String getUserStatus(){return userStatus;}

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setTime(String time){
        this.time = time;
    }
    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public boolean isUsername(String username) {
        System.out.println(this.username + username);
        return this.username.equals(username);
    }
    public boolean isPassword(String password) {
        return this.password.equals(password);
    }
    public boolean isAdmin(String role){
        return "admin".equals(role);
    }
    public boolean isUnban(String userStatus){
        return "unban".equals(userStatus);
    }
    @Override
    public String toString(){
        return "Role: " + role + " Username: " + username + " Last Online: " + time + " Status: " + userStatus;
    }
}
