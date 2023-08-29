package cs211.project.models;

import java.util.Date;

public class User {
    private String username;
    private String name;
    private String password;
    private static int idUser = 1_000_000;
    private final int id;
    private String time;
    private String pictureURL;

    public User(String username, String name, String password, String time){
        this.username = username;
        this.name = name;
        this.password = password;
        this.time = time;
        id = idUser++;
        pictureURL = null;
    }
    public User(String username, String name, String password){
        this.username = username;
        this.name = name;
        this.password = password;
        id = idUser++;
        pictureURL = null;
    }//เอาออกหลังแก้ model admin
    public User(int id, String username, String name, String password){
        this.username = username;
        this.name = name;
        this.password = password;
        this.id = id;
        pictureURL = null;
    }//สำหรับโรลแอดมิน

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

    public int getId() {
        return id;
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
    public String checkRole(int id){
        if(id > 10){
            return "user";
        }else {
            return "admin";
        }
    }

}
