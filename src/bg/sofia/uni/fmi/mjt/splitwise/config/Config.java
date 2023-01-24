package bg.sofia.uni.fmi.mjt.splitwise.config;

import java.net.InetAddress;

public class Config {
    public static final String USERS_PATH = "users.json";
    public static final String GROUPS_FOLDER = "groups/";
    public static final int REMOTE_PORT = 7462;
    public static final InetAddress REMOTE_HOST = InetAddress.getLoopbackAddress();
    public static final String LOG_FILE_SERVER = "server.log";
    public static final String LOG_FILE_CLIENT = "client.log";
}
