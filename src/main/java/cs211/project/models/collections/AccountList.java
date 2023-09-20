package cs211.project.models.collections;
import cs211.project.models.Account;

import java.util.ArrayList;
public class AccountList{
    private ArrayList<Account> accounts;
    public AccountList() {accounts = new ArrayList<>();}
    public void addNewAccount(int id, String username, String password, String name, String time, String picURL, String userStatus, String role) {
        username = username.trim();
        name = name.trim();
        password = password.trim();
        if (!username.equals("")&&!name.equals("")) {
            Account exist = findAccountByUsername(username);
            if (exist == null) {
                accounts.add(new Account(id, username, password, name, time.trim(), picURL.trim(), userStatus.trim(), role.trim()));
            }
        }
    }

    public void addUserEvent(int id, String eventName) {
        eventName = eventName.trim();
        if (!eventName.equals("")) {
            Account exist = findAccountById(id);
            if (exist != null) {
                String event = findEventByEventName(exist, eventName);
                if (event == null) {
                    exist.addUserEventName(eventName);
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

    public Account findAccountById(int id) {
        for (Account account : accounts) {
            if (account.isId(id)) {
                return account;
            }
        }
        return null;
    }

    public Account findAccountByUsername(String username) {
        for (Account account : accounts) {
            if (account.isUsername(username)) {
                return account;
            }
        }
        return null;
    }

    public ArrayList<Account> getAccount(){
        return accounts;
    }
}
