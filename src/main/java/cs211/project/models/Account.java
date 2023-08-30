package cs211.project.models;

public class Account {
    private int id;
    private String username;
    private String password;
    private String name;
    private String time;
    private String pictureURL;


    public Account(int id, String username, String password, String name, String time) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.time = time;
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

    public boolean isUsername(String username) {
        return this.username.equals(username);
    }
    public boolean isPassword(String password) {
        return this.password.equals(password);
    }
    public boolean isId(int id){return this.id == id;}
    public String checkRole(int id){
        if(id > 10){return "user";}
        else {return "admin";}
    }
    @Override
    public String toString(){
        return "ID: " + id + " Username: " + username + " Last Online: " + time;
    }
}
