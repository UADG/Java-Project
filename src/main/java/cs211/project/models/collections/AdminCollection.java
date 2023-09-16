package cs211.project.models.collections;

import cs211.project.models.Admin;
import cs211.project.models.User;

import java.util.ArrayList;

public class AdminCollection {
    private ArrayList<Admin> admins;
    public AdminCollection() {
        admins = new ArrayList<>();
    }
    public void addNewAdmin(String username, String password, String name, String time, String picURL) {
        username = username.trim();
        name = name.trim();
        password = password.trim();

        if (!username.equals("")) {
            Admin exist = findAdminByUsername(username);
            if (exist == null) {
                admins.add(new Admin(username, password, name.trim(), time.trim(), picURL.trim()));
            }
        }
    }

    public Admin findAdminByUsername(String username) {
        for (Admin admin : admins) {
            if (admin.isUsername(username)) {
                return admin;
            }
        }
        return null;
    }
    public ArrayList<Admin> getAdmins(){
        return admins;
    }
}
