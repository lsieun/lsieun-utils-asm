package lsieun.utils.archive;

import lsieun.utils.io.dir.DirNioUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ZipFindNioUtilsTest {
    @Test
    void testFindJarByEntry() throws IOException {
        Path dirPath = Path.of("D:\\ideaIU-2024.2.1.win\\lib");
        List<Path> pathList = DirNioUtils.findFileListInDirByExt(dirPath, 1,".jar");
        List<Path> jarList = ZipFindNioUtils.findJarByEntry(pathList, List.of("com/intellij/", "com/jetbrains/"));
        for (Path jarPath : jarList) {
            System.out.println(dirPath.relativize(jarPath));
        }
    }
}