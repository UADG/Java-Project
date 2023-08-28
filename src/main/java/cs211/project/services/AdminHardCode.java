package cs211.project.services;

import cs211.project.models.collections.AdminCollection;

public class AdminHardCode {
    public AdminCollection readData() {
        AdminCollection list = new AdminCollection();
        list.addNewAdmin(0, "Admin", "admin1234", "Ad");

        return list;
    }
}
