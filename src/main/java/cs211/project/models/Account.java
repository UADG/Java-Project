package cs211.project.models;

public class Account {
    protected String username;
    protected String name;
    protected String password;
    protected static int idUser = 1_000_000;
    protected final int id;
    protected String time;
    protected String pictureURL;


    public Account(int id, String username, String password, String name, String time) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.time = time;
        this.pictureURL = getClass().getResource("/images/default-profile.png").toExternalForm();
        System.out.println(pictureURL);
    }
    public Account(String username, String name, String password, String time){
        this.username = username;
        this.name = name;
        this.password = password;
        this.time = time;
        this.id = idUser++;
        this.pictureURL = getClass().getResource("/images/default-profile.png").toExternalForm();
        System.out.println(pictureURL);
    }
    public Account(int id, String username, String name, String password){
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.time ="";
        this.pictureURL = getClass().getResource("/images/default-profile.png").toExternalForm();
        System.out.println(pictureURL);
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
