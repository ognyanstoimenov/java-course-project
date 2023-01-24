package bg.sofia.uni.fmi.mjt.splitwise.server.repository;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public abstract class Repository<T> {

    private final Class<T[]> arrType;
    private final Gson gson;

    protected Repository(Class<T[]> arrType) {
        this.arrType = arrType;
        this.gson = new Gson();
    }

    protected List<T> getFromPath(Path path) {
        try (BufferedReader reader = getReader(path)) {
            return new ArrayList<>(List.of(gson.fromJson(reader, arrType)));
        } catch (NoSuchFileException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read from file", e);
        }
    }

    protected void replaceInPath(Path path, List<T> newContent) {
        try (BufferedWriter writer = getWriter(path)) {
            gson.toJson(newContent, writer);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write to file.", e);
        }
    }

    private BufferedReader getReader(Path path) throws IOException {
        return Files.newBufferedReader(path);
    }

    private BufferedWriter getWriter(Path path) throws IOException {
        return Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

}
