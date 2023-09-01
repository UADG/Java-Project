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
        accountList.addNewAccount(11,"best", "Bestttt", "Bestto", formatted1);
        accountList.addNewAccount(12,"win", "winwin", "WinnieThePooh", formatted2);
        accountList.addNewAccount(13,"ai", "12345678", "AiAye", formatted3);
        accountList.addNewAccount(14,"jim", "JimZa007", "JimZaKung", formatted4);
        accountList.addNewAccount(0, "Admin", "admin1234", "Ad", "");
        accountList.addNewAccount(1,"admin", "123", "a", "");
        return accountList;
    }
}