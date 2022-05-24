package dao;

/**
 * Created by admin on 2021/12/22.
 */

public class User {
    String User;
    String Password;

    public User(String user, String password) {
        User = user;
        Password = password;

    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "User='" + User + '\'' +
                ", Password='" + Password + '\'' +
                '}';
    }
}
