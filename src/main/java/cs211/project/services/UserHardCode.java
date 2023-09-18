package cs211.project.services;

import cs211.project.models.collections.UserList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserHardCode {

    public UserList readData() {
        UserList list = new UserList();

        LocalDateTime time1 = LocalDateTime.of(2023, 5, 5, 0, 0);
        LocalDateTime time2 = LocalDateTime.of(2023, 5, 5, 0, 0);
        LocalDateTime time3 = LocalDateTime.of(2023, 5, 5, 10, 0);
        LocalDateTime time4 = LocalDateTime.of(2023, 5, 5, 12, 0);
        LocalDateTime time5 = LocalDateTime.of(2023, 5, 5, 9, 0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        String formatted1 = time1.format(formatter);
        String formatted2 = time2.format(formatter);
        String formatted3 = time3.format(formatter);
        String formatted4 = time4.format(formatter);
//
//        list.addNewUser("best", "Bestttt", "Bestto", formatted1);
//        list.addNewUser("win", "winwin", "WinnieThePooh", formatted2);
//        list.addNewUser("ai", "12345678", "AiAye", formatted3);
//        list.addNewUser("jim", "jimza007", "JimZaKung", formatted4);

        return list;
    }
}