package lsieun.utils.archive;

import lsieun.utils.io.dir.DirNioUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

class ZipFindNioUtilsTest {
    @Test
    void testFindFileList() throws IOException {
        Path dirPath = Path.of("D:\\ideaIU-2024.2.1.win\\lib");
        List<Path> fileList = DirNioUtils.findFileListInDirByExt(dirPath, 1,".jar");
        List<Path> candidateList = ZipFindNioUtilsForMultiple.findFileList(fileList, List.of("com/intellij/", "com/jetbrains/"));
        for (Path jarPath : candidateList) {
            System.out.println(dirPath.relativize(jarPath));
        }
    }
}