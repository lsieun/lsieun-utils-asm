package lsieun.utils.match.archive;

import lsieun.utils.match.bytes.ByteArrayMatch;
import lsieun.utils.match.text.TextMatch;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

public interface ZipEntryMatch {
    boolean test(FileSystem zipFileSystem, String entry);

    static ZipEntryMatch byText(TextMatch match) {
        return (zipFs, entry) -> match.test(entry);
    }

    static ZipEntryMatch byBytes(ByteArrayMatch match) {
        return (zipFs, entry) -> {
            try {
                Path path = zipFs.getPath(entry);
                byte[] bytes = Files.readAllBytes(path);
                return match.test(bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
