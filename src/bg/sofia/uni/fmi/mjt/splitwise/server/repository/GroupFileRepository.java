package bg.sofia.uni.fmi.mjt.splitwise.server.repository;

import bg.sofia.uni.fmi.mjt.splitwise.models.Group;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupFileRepository extends Repository<Group> {

    private final Path groupFolder;

    public GroupFileRepository(String groupsFolder) throws IOException {
        super(Group[].class);
        this.groupFolder = Path.of(groupsFolder);
        Files.createDirectories(Paths.get(groupsFolder));
    }

    public List<Group> getGroups() {
        List<Group> result = new ArrayList<>();
        Arrays.stream(groupFolder.toFile().listFiles())
                .forEach(file -> result.addAll(getFromPath(file.toPath())));

        return result;
    }

    public void updateGroup(Group group) {
        replaceInPath(getGroupPath(group), List.of(group));
    }

    private Path getGroupPath(Group group) {
        return groupFolder.resolve(group.getId().toString() + ".json");
    }

}
