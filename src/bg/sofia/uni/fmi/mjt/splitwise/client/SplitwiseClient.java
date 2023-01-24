package bg.sofia.uni.fmi.mjt.splitwise.client;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class SplitwiseClient {
    private final InetSocketAddress socketAddress;

    private boolean connected;
    private SocketChannel socketChannel;
    private BufferedReader reader;
    private PrintWriter writer;

    private final Gson gson = new Gson();

    public SplitwiseClient(InetSocketAddress socketAddress) {
        if (socketAddress == null) {
            throw new IllegalArgumentException("Socket address cannot be null");
        }
        this.socketAddress = socketAddress;
    }

    public void connect() {
        if (connected) {
            return;
        }
        try {
            socketChannel = SocketChannel.open();
            reader = new BufferedReader(Channels.newReader(socketChannel, StandardCharsets.UTF_8));
            writer = new PrintWriter(Channels.newWriter(socketChannel, StandardCharsets.UTF_8), true);
            socketChannel.connect(socketAddress);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot connect to server.", e);
        }
        connected = true;
    }

    public void sendCommand(String command) {
        writer.println(command);
    }

    public String receiveResponse() {
        try {
            return gson.fromJson(reader.readLine(), String.class);
        }
        catch (IOException e) {
            throw new UncheckedIOException("Couldn't get server response.", e);
        }
    }

    public void disconnect() {
        try {
            socketChannel.close();
        } catch (IOException e) {
            throw new UncheckedIOException("Couldn't disconnect from server", e);
        }
    }
}
