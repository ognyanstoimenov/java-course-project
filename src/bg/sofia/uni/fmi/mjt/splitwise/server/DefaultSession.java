package bg.sofia.uni.fmi.mjt.splitwise.server;

public class DefaultSession implements Session {
    private String currentUser;

    public String getCurrentUser() {
        return currentUser;
    }

    @Override
    public void setCurrentUser(String username) {
        this.currentUser = username;
    }
}
