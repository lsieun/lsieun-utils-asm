package lsieun.utils.io.file;

import lsieun.annotation.type.UtilityClass;
import lsieun.utils.log.Logger;
import lsieun.utils.log.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@UtilityClass
public class FileNioUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileNioUtils.class);

    public static void copy(Path srcPath, Path dstPath) throws IOException {
        if (!Files.exists(srcPath)) {
            throw new IllegalArgumentException("Source path does not exist: " + srcPath);
        }
        if (!Files.exists(dstPath)) {
            Path parent = dstPath.getParent();
            Files.createDirectories(parent);
        }

        Files.copy(srcPath, dstPath, StandardCopyOption.REPLACE_EXISTING);
        logger.info(() -> FileFormatUtils.format(srcPath, dstPath, FileOperation.COPY));
    }
}