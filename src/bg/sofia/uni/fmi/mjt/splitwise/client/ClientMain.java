package bg.sofia.uni.fmi.mjt.splitwise.client;

import bg.sofia.uni.fmi.mjt.splitwise.config.Config;

import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.logging.Logger;

public class ClientMain {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(ClientMain.class.getName());

        var remoteAddress = new InetSocketAddress(Config.REMOTE_HOST, Config.REMOTE_PORT);
        var connection = new SplitwiseClient(remoteAddress);
        logger.info("Connecting to server...");

        connection.connect();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter command: ");
            String command = scanner.nextLine();
            if (command.equals("exit")) {
                connection.disconnect();
                return;
            }
            connection.sendCommand(command);

            try {
                System.out.println(connection.receiveResponse());
            }
            catch (UncheckedIOException e) {
                logger.severe(e.getMessage());
            }
        }

    }
}
