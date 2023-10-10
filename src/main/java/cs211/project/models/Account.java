package cs211.project.models;

//import at.favre.lib.crypto.bcrypt.BCrypt;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Account implements Comparable<Account>{
    protected String username;
    protected String name;
    protected String password;
    protected String role;
    protected String time;
    protected String pictureURL;
    protected ArrayList<String> allEventUser;
    protected final int id;

    public Account(int id, String username, String password, String name, String time, String pictureURL, String role) {
        this.id = id;
        this.role = role;
        this.username = username;
        this.name = name;
        this.password = password;
        this.time = time;
        this.pictureURL = pictureURL;
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

    public void changeName(String oldName, String newName) {
        for (int i = 0; i < allEventUser.size(); i++) {
            if (oldName.equals(allEventUser.get(i))) {
                allEventUser.set(i, newName);
            }
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

    public boolean isId(int id) {
        return this.id == id;
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
    public int getId(){return id;}
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
    public boolean isName(String name) {
        return this.name.equals(name);
    }
    public boolean isAdmin(String role) {
        return "admin".equals(role);
    }
//    public boolean validatePassword(String password) {
//        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), this.password);
//        return result.verified;
//    }
    @Override
    public String toString() {
        return "ID: " + id + " Username: " + username + " Last Online: " + time;
    }

    @Override
    public int compareTo(Account o) {
        String pattern = "yyyy-MM-dd' 'HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime userDateTime = LocalDateTime.parse(time, formatter);
        LocalDateTime oDateTime = LocalDateTime.parse(o.time, formatter);

        if (userDateTime.isBefore(oDateTime)) {
            return 1;
        } else if (userDateTime.isAfter(oDateTime)) {
            return -1;
        } else {
            return 0;
        }
    }
}
