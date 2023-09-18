package cs211.project.models.collections;
import cs211.project.models.Account;

import java.util.ArrayList;
public class AccountList{
    private ArrayList<Account> accounts;
    public AccountList() {accounts = new ArrayList<>();}
    public void addNewAccount(String role, String username, String password, String name, String time, String picURL, String userStatus) {
        username = username.trim();
        name = name.trim();
        password = password.trim();
        if (!username.equals("")&&!name.equals("")) {
            Account exist = findAccountByUsername(username);
            if (exist == null) {
                accounts.add(new Account(role.trim(), username, password, name, time.trim(), picURL.trim(), userStatus.trim()));
            }
        }
    }

    public void addUserEvent(String username, String eventName) {
        username = username.trim();
        eventName = eventName.trim();
        if (!username.equals("") && !eventName.equals("")) {
            Account exist = findAccountByUsername(username);
            System.out.println(exist);
            if (exist != null) {
                String event = findEventByEventName(exist, eventName);
                if (event == null) {
                    exist.addUserEventName(eventName);
                }
            }
        }
    }

    public void deleteEvent(String username, String eventName) {
        username = username.trim();
        eventName = eventName.trim();
        if (!username.equals("") && !eventName.equals("")) {
            Account exist = findAccountByUsername(username);
            if (exist != null) {
                String event = findEventByEventName(exist, eventName);
                if (event == null) {
                    exist.deleteUserEventName(eventName);
                }
            }
        }
    }

    public String findEventByEventName(Account exist, String eventName) {
        if (exist.isEventName(eventName)) {
            return eventName;
        }
        return null;
    }

    public Account findAccountByUsername(String username) {
        System.out.println(accounts);
        for (Account account : accounts) {
            System.out.println("Comparing: " + username + " with " + account.getUsername());
            if (account.isUsername(username)) {
                return account;
            }
        }
        System.out.println("Account not found for username: " + username);
        return null;
    }

    public ArrayList<Account> getAccount(){
        return accounts;
    }
}
