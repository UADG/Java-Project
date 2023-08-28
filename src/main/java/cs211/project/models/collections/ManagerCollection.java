package cs211.project.models.collections;

import cs211.project.models.Manager;

import java.util.ArrayList;

public class ManagerCollection {
    private ArrayList<Manager> managers;

    public ManagerCollection() {
        managers = new ArrayList<>();
    }

    public void addManager(int id, String username, String password, String name) {
        username = username.trim();
        name = name.trim();
        if (!username.equals("") && !name.equals("")) {
            Manager exist = findManagerById(id);
            if (exist == null) {
                managers.add(new Manager(username, password, name));
            }
        }
    }

    public Manager findManagerById(int id) {
        for (Manager manager : managers) {
            if (manager.isId(id)) {
                return manager;
            }
        }
        return null;
    }

    public ArrayList<Manager> getManagers() {
        return managers;
    }
}
