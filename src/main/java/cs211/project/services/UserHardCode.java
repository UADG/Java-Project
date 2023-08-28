package cs211.project.services;

import cs211.project.models.UserList;

public class UserHardCode {

    public UserList readData() {
        UserList list = new UserList();
        list.addNewUser("best", "Bestto","Bestttt");
        list.addNewUser("win", "WinnieThepooh","winwin");
        list.addNewUser("ai", "AiAye","12345678");
        list.addNewUser("jim", "JimZaKung","JimZa007");

        return list;
    }
}