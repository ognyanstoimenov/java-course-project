package bg.sofia.uni.fmi.mjt.splitwise.models;

import bg.sofia.uni.fmi.mjt.splitwise.utils.DateTimeUtils;

import java.time.LocalDateTime;

public class User {
    private final String username;
    private final String password;

    private String lastLoggedIn;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.lastLoggedIn = null;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getLastLoggedIn() {
        return DateTimeUtils.stringToDate(lastLoggedIn);
    }

    public void logIn() {
        lastLoggedIn = DateTimeUtils.dateToString(LocalDateTime.now());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof final User other)) {
            return false;
        }

        return username.equals(other.username) && password.equals(other.password);
    }

    @Override
    public int hashCode() {
        return username.hashCode() + password.hashCode();
    }
}
