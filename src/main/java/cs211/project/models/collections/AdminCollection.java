package cs211.project.models.collections;

import cs211.project.models.Admin;

import java.util.ArrayList;

public class AdminCollection {
    private ArrayList<Admin> admins;
    public AdminCollection() {
        admins = new ArrayList<>();
    }
    public void addNewAdmin(int id, String username, String password, String name) {
        username = username.trim();
        name = name.trim();
        password = password.trim();

        if (!username.equals("")) {
            Admin exist = findAdminById(id);
            if (exist == null) {
                admins.add(new Admin(username, name, password));
            }
        }
    }
    public Admin findAdminById(int id) {
        for (Admin admin : admins) {
            if (admin.isId(id)) {
                return admin;
            }
        }
        return null;
    }
    public ArrayList<Admin> getAdmins(){
        return admins;
    }
}
