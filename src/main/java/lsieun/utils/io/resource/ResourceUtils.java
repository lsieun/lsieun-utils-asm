package lsieun.utils.io.resource;

import lsieun.utils.io.IOUtils;
import lsieun.utils.io.file.FileFormatUtils;
import lsieun.utils.io.file.FileOperation;
import lsieun.utils.log.Logger;
import lsieun.utils.log.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;

public class ResourceUtils {
    private static final Logger logger = LoggerFactory.getLogger(ResourceUtils.class);

    public static Path readFilePath(String filepath) {
        ResourceStrategy[] values = ResourceStrategy.values();
        int length = values.length;
        for (int i = 0; i < length; i++) {
            ResourceStrategy strategy = values[i];
            Optional<Path> op = strategy.parse(filepath);
            if (op.isPresent()) {
                Path path = op.get();
                logger.debug(() -> FileFormatUtils.format(path, FileOperation.FIND));
                return path;
            }
        }

        String msg = String.format("filepath NOT FOUND: %s", filepath);
        throw new IllegalArgumentException(msg);
    }

    public static byte[] readClassBytes(Class<?> clazz) throws IOException {
        String typeName = clazz.getTypeName();
        String resourcePath = typeName.replace('.', '/') + ".class";
        try (InputStream in = ClassLoader.getSystemResourceAsStream(resourcePath)) {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            IOUtils.copy(in, bao);
            return bao.toByteArray();
        }
    }
}