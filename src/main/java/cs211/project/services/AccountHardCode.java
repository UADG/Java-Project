package cs211.project.services;

import cs211.project.models.collections.AccountList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccountHardCode {

    public AccountList readData() {

        LocalDateTime time1 = LocalDateTime.of(2023, 5, 5, 0, 0);
        LocalDateTime time2 = LocalDateTime.of(2023, 5, 5, 0, 0);
        LocalDateTime time3 = LocalDateTime.of(2023, 5, 5, 10, 0);
        LocalDateTime time4 = LocalDateTime.of(2023, 5, 5, 12, 0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        String formatted1 = time1.format(formatter);
        String formatted2 = time2.format(formatter);
        String formatted3 = time3.format(formatter);
        String formatted4 = time4.format(formatter);

        AccountList accountList = new AccountList();
        accountList.addNewAccount("user","best", "Bestttt", "Bestto", formatted1, "-","unban");
        accountList.addNewAccount("user","win", "winwin", "WinnieThePooh", formatted2, "-","unban");
        accountList.addNewAccount("user","ai", "12345678", "AiAye", formatted3, "-","unban");
        accountList.addNewAccount("user","jim", "JimZa007", "JimZaKung", formatted4, "-","unban");
        accountList.addNewAccount("admin", "Admin", "admin1234", "Ad", "", "-","unban");
        accountList.addNewAccount("admin","admin", "123", "a", "", "-","unban");
        return accountList;
    }
}