package lsieun.utils.match.archive;

import lsieun.utils.match.text.TextMatch;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@FunctionalInterface
public interface ArchivePathMatch {
    boolean test(Path path);

    static ArchivePathMatch byFileName(TextMatch textMatch) {
        Function<Path, String> func = filepath -> filepath.getFileName().toString();
        return byText(func, textMatch);
    }

    static ArchivePathMatch byText(Function<Path, String> func, TextMatch textMatch) {
        return path -> textMatch.test(func.apply(path));
    }

    static ArchivePathMatch byZipFileSystem(ZipFileSystemMatch zipFileSystemMatch) {
        return path -> {
            // (1) uri
            URI zipUri = URI.create("jar:" + path.toUri());

            // (2) env
            Map<String, String> env = new HashMap<>(1);
            env.put("create", "false");

            // (3) use
            try (FileSystem zipFs = FileSystems.newFileSystem(zipUri, env)) {
                return zipFileSystemMatch.test(zipFs);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
