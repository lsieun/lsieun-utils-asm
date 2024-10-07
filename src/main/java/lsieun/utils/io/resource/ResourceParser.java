package lsieun.utils.io.resource;

import java.nio.file.Path;
import java.util.Optional;

public interface ResourceParser {
    Optional<Path> parse(String filepath);
}
