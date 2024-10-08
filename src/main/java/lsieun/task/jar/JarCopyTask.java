package lsieun.task.jar;

import lsieun.utils.archive.ZipFindNioUtils;
import lsieun.utils.ds.pair.Pair;
import lsieun.utils.ds.pair.PairBuddy;
import lsieun.utils.io.dir.DirNioUtils;
import lsieun.utils.io.file.FileNioUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JarCopyTask {
    public static void copyJarByClass(Path srcPath, Path dstPath, String... classArray) throws IOException {
        // source dir
        if (!Files.exists(srcPath)) return;
        if (!Files.isDirectory(srcPath)) return;

        // class array
        if (classArray == null || classArray.length == 0) return;

        // jar list
        List<Path> jarPathList = DirNioUtils.findFileListInDirByExt(srcPath, ".jar");
        if (jarPathList.isEmpty()) return;

        // pair list
        List<Pair<String, Path>> pairList = ZipFindNioUtils.findClass(jarPathList, classArray);
        if (pairList.isEmpty()) return;

        // map
        Map<String, List<Path>> groupMap = PairBuddy.groupToMap(pairList, Pair::first, Pair::second);
        Map<Path, Long> countMap = PairBuddy.countToMap(pairList, Pair::second);

        // one to one
        List<Pair<String, Path>> one2OnePairList = one2oneList(groupMap, countMap);
        one2OnePairList.forEach(pair -> {
            System.out.println(pair.first() + " " + pair.second());
        });

        // path list
        List<Path> pathList = one2OnePairList.stream().map(Pair::second).distinct().toList();

        // move file
        for (Path path : pathList) {
            Path fileName = path.getFileName();
            Path newFilePath = dstPath.resolve(fileName);
            FileNioUtils.copy(path, newFilePath);
        }
    }

    private static List<Pair<String, Path>> one2oneList(Map<String, List<Path>> groupMap, Map<Path, Long> countMap) {
        List<Pair<String, Path>> pairList = new ArrayList<>();
        groupMap.forEach((entry, list) -> {
            Path path = getOnePath(list, countMap);
            Pair<String, Path> pair = new Pair<>(entry, path);
            pairList.add(pair);
        });
        return pairList;
    }

    private static Path getOnePath(List<Path> pathList, Map<Path, Long> countMap) {
        Path targetPath = pathList.get(0);
        for (int i = 1; i < pathList.size(); i++) {
            Path path = pathList.get(i);
            targetPath = getOnePath(targetPath, path, countMap);
        }
        return targetPath;
    }

    private static Path getOnePath(Path firstPath, Path secondPath, Map<Path, Long> countMap) {
        long count1 = countMap.get(firstPath);
        long count2 = countMap.get(secondPath);
        if (count1 > count2) return firstPath;
        if (count1 < count2) return secondPath;

        int nameCount1 = firstPath.getNameCount();
        int nameCount2 = secondPath.getNameCount();
        if (nameCount1 < nameCount2) return firstPath;
        if (nameCount1 > nameCount2) return secondPath;

        int length1 = firstPath.toString().length();
        int length2 = secondPath.toString().length();
        if (length1 < length2) return firstPath;
        if (length1 > length2) return secondPath;
        return firstPath;
    }
}
