package cs211.project.models.collections;
import cs211.project.models.Account;

import java.util.ArrayList;
public class AccountList{
    private ArrayList<Account> accounts;
    public AccountList() {accounts = new ArrayList<>();}
    public void addNewAccount(String role, String username, String password, String name, String time, String picURL) {
        username = username.trim();
        name = name.trim();
        password = password.trim();
        if (!username.equals("")&&!name.equals("")) {
            Account exist = findAccountByUsername(username);
            if (exist == null) {
                accounts.add(new Account(role.trim(), username, password, name, time.trim(), picURL.trim()));
            }
        }
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
