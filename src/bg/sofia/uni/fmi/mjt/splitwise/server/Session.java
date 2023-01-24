package bg.sofia.uni.fmi.mjt.splitwise.server;

public interface Session {
    String getCurrentUser();

    void setCurrentUser(String username);
}
