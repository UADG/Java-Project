package cs211.project.models;

import java.util.Date;

public class User {
    private String username;
    private String name;
    private String password;
    private static int idUser = 1_000_000;
    private int id;
    private Date time;
    private String pictureURL;

    public User(String username, String name, String password){
        this.username = username;
        this.name = name;
        this.password = password;
        id = idUser++;
        time = null;
        pictureURL = null;
    }
    public String getUsername(){
        return username;
    }
    public String getName(){
        return name;
    }
    public String getPassword() {
        return password;
    }
    public Date getTime(){
        return time;
    }
    public String getPictureURL(){
        return pictureURL;
    }

    public int getId() {
        return id;
    }

    public void setPassword(String password){
        this.password = password;
    }
    public void setTime(Date time){
        this.time = time;
    }
    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }
    @Override
    public String toString(){
        return "ID: " + id + " Username: " + username + " Last Online: " + time;
    }

    public boolean isUsername(String username) {
        return this.username.equals(username);
    }
    public boolean isPassword(String password) {
        return this.password.equals(password);
    }
    public boolean isId(int id){return this.id == id;}

}
