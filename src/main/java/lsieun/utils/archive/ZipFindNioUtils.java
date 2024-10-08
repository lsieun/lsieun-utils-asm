package lsieun.utils.archive;

import lsieun.annotation.method.MethodParamExample;
import lsieun.annotation.mind.blueprint.Intention;
import lsieun.utils.ds.pair.Pair;
import lsieun.utils.log.Logger;
import lsieun.utils.log.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <pre>
 *                                                        ┌─── getEntryListByExtension()
 *                    ┌─── file ───┼─── getEntryList() ───┤
 * ZipFindNioUtils ───┤                                   └─── getClassList() ──────────────┼─── getClassNameList()
 *                    │
 *                    └─── dir ────┼─── findEntry() ───┼─── findClass()
 * </pre>
 */
public class ZipFindNioUtils {
    private static final Logger logger = LoggerFactory.getLogger(ZipFindNioUtils.class);

    public static List<String> getEntryListByExtension(Path jarPath,
                                                       @MethodParamExample({".class", ".java"}) String ext) throws IOException {
        BiPredicate<Path, BasicFileAttributes> predicate = (path, attrs) ->
                attrs.isRegularFile() && path.getFileName().toString().endsWith(ext);
        return getEntryList(jarPath, predicate);
    }

    public static List<String> getClassList(Path jarPath) throws IOException {
        BiPredicate<Path, BasicFileAttributes> predicate = (path, attrs) -> {
            if (!attrs.isRegularFile()) {
                return false;
            }

            String filename = path.getFileName().toString();

            if (!filename.endsWith(".class")) {
                return false;
            }

            if (filename.equals("package-info.class")) {
                return false;
            }

            if (filename.contains("$")) {
                return false;
            }

            return true;
        };

        return getEntryList(jarPath, predicate);
    }

    public static List<String> getEntryList(Path jarPath,
                                            BiPredicate<Path, BasicFileAttributes> predicate) throws IOException {
        if (jarPath == null) {
            throw new IllegalArgumentException("jarPath is null");
        }
        if (!Files.exists(jarPath)) {
            throw new IllegalArgumentException("jar does not exist: " + jarPath);
        }
        if (!Files.isRegularFile(jarPath)) {
            throw new IllegalArgumentException("jar is not a file: " + jarPath);
        }

        URI zipUri = URI.create("jar:" + jarPath.toUri());

        Map<String, String> env = new HashMap<>(1);
        env.put("create", "false");

        try (FileSystem zipfs = FileSystems.newFileSystem(zipUri, env)) {
            Path dirPath = zipfs.getPath("/");

            try (Stream<Path> stream = Files.find(dirPath, Integer.MAX_VALUE, predicate)) {
                return stream
                        .map(Path::toString)
                        .sorted()
                        .collect(Collectors.toList());
            }
        }
    }

    public static List<String> getClassNameList(Path jarPath) throws IOException {
        List<String> classList = getClassList(jarPath);
        int size = classList.size();

        List<String> nameList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String entry = classList.get(i);
            int index = entry.lastIndexOf(".");
            if (entry.startsWith("/")) {
                entry = entry.substring(1, index);
            }
            else {
                entry = entry.substring(0, index);
            }
            String name = entry.replace('/', '.');
            nameList.add(name);
        }
        Collections.sort(nameList);
        return nameList;
    }

    public static List<Path> findJarByEntry(List<Path> pathList, List<String> entryList) {
        List<Pair<String, Path>> pairList = findEntry(pathList, entryList);
        return pairList.stream().map(Pair::second).distinct().toList();
    }

    public static List<Pair<String, Path>> findClass(List<Path> pathList, String[] classnames) {
        List<String> entryList = Arrays.stream(classnames)
                .map(name -> name.replace(".", "/") + ".class")
                .toList();
        return findEntry(pathList, entryList);
    }

    @Intention({
            "Pair 中 String 存储的是 com/abc/Xyz.class",
            "Pair 中 Path 存储是 jar 包的路径"
    })
    public static List<Pair<String, Path>> findEntry(List<Path> pathList, List<String> entryList) {
        Objects.requireNonNull(pathList);
        Objects.requireNonNull(entryList);

        if (pathList.isEmpty()) {
            return Collections.emptyList();
        }

        int size = pathList.size();

        logger.debug(() -> String.format("Total Jars: %d", size));


        List<Pair<String, Path>> resultList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Path jarPath = pathList.get(i);
            int num = i + 1;
            logger.debug(() -> String.format("[PROCESS] %03d - %s", num, jarPath));

            URI zipUri = URI.create("jar:" + jarPath.toUri());

            Map<String, String> env = new HashMap<>(1);
            env.put("create", "false");

            try (FileSystem zipFs = FileSystems.newFileSystem(zipUri, env)) {
                for (String entry : entryList) {

                    Path entryPath = zipFs.getPath(entry);
                    if (Files.exists(entryPath)) {
                        resultList.add(new Pair<>(entry, jarPath));
                    }
                }
            } catch (Exception e) {
                String msg = String.format("something is wrong: %s", e.getMessage());
                System.out.println(msg);
            }
        }
        return resultList;
    }
}
