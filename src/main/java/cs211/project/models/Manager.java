package cs211.project.models;

public class Manager {

    private static int idManager = 1_000_000;
    private String username;
    private String password;
    private String name;

    public Manager(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
        idManager++;
    }

    public boolean isId(int id) {
        return idManager == id;
    }

    public int getId() {
        return idManager;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

}
