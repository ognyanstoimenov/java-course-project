package bg.sofia.uni.fmi.mjt.splitwise.server;

import bg.sofia.uni.fmi.mjt.splitwise.config.Config;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CommandException;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import java.util.logging.Level;

public class SplitwiseServer {
    private static final int BUFFER_SIZE = 1024;

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final CommandManager commandManager;
    private final Gson gson = new Gson();


    private boolean running;
    private ByteBuffer buffer;
    private Selector selector;

    public SplitwiseServer(CommandManager commandManager) throws IOException {
        this.commandManager = commandManager;
        logger.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
        FileHandler fileHandler = new FileHandler(Config.LOG_FILE_SERVER);
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
    }

    public void run() {
        logger.info("Server starting.");
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            selector = Selector.open();
            serverSocketChannel.bind(new InetSocketAddress(Config.REMOTE_HOST, Config.REMOTE_PORT));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            buffer = ByteBuffer.allocate(BUFFER_SIZE);
            running = true;
            while (running) {
                try {
                    if (selector.select() == 0) {
                        continue;
                    }
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        if (key.isReadable()) {
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            String input = getInput(socketChannel);
                            if (input == null) {
                                continue;
                            }
                            Session session = (Session) key.attachment();
                            input = input.replace("\n", "");
                            List<String> arguments = Arrays.stream(input.split(" ")).toList();
                            String command = arguments.get(0);
                            List<String> params = arguments.subList(1, arguments.size());

                            String response;
                            try {
                                response = commandManager.getCommand(command).run(params, session);
                            } catch (CommandException e) {
                                response = e.getMessage();
                            } catch (Exception e) {
                                response = "Unknown error.";
                                logger.log(Level.SEVERE, "Command error: ", e);
                            }

                            String jsonString = gson.toJson(response);

                            writeOutput(socketChannel, jsonString);
                        }
                        else if (key.isAcceptable()) {
                            accept(selector, key);
                        }
                        keyIterator.remove();
                    }
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Cannot connect to client.", e);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Unknown error", e);
                    stop();
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Couldn't start server", e);
        }
    }

    public void stop() {
        running = false;
        if (selector.isOpen()) {
            selector.wakeup();
        }
    }

    private String getInput(SocketChannel channel) throws IOException {
        buffer.clear();

        int readBytes = channel.read(buffer);
        if (readBytes < 0) {
            return null;
        }
        buffer.flip();
        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void writeOutput(SocketChannel channel, String output) throws IOException {
        buffer.clear();
        buffer.put(output.getBytes(StandardCharsets.UTF_8));
        buffer.put(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
        buffer.flip();

        channel.write(buffer);
    }


    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverSocketChannel.accept();
        channel.configureBlocking(false);

        SelectionKey readKey = channel.register(selector, SelectionKey.OP_READ);
        readKey.attach(new DefaultSession());
    }
}
