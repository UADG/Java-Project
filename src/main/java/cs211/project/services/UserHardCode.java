package cs211.project.services;

import cs211.project.models.UserList;

public class UserHardCode {

    public UserList readData() {
        UserList list = new UserList();
        list.addNewUser(0, "best", "Bestttt", "Bestto");
        list.addNewUser(0, "win", "winwin", "WinnieThepooh");
        list.addNewUser(0, "ai", "12345678", "AiAye");
        list.addNewUser(0, "jim", "JimZa007", "JimZaKung");

        return list;
    }
}