package lsieun.utils.archive;

import lsieun.utils.ds.pair.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.BiConsumer;

public class ZipContentNioUtils {
    public static List<Pair<Path, Integer>> readAllClassFileVersionInDir(List<Path> jarPathList) throws IOException {
        Objects.requireNonNull(jarPathList);
        if (jarPathList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Pair<Path, Integer>> list = new ArrayList<>();
        for (Path jarPath : jarPathList) {
            List<Integer> versionList = readAllClassFileVersion(jarPath);
            if (!versionList.isEmpty()) {
                for (Integer version : versionList) {
                    list.add(new Pair<>(jarPath, version));
                }
            }
        }
        return list;
    }

    public static List<Integer> readAllClassFileVersion(Path zipPath) throws IOException {
        List<Path> classList = ZipFindNioUtils.getClassList(zipPath);
        List<String> entryList = classList.stream().map(Path::toString).toList();
        List<Pair<String, byte[]>> pairList = readEntryList(zipPath, entryList);

        List<Integer> list = new ArrayList<>();
        for (Pair<String, byte[]> pair : pairList) {
            byte[] bytes = pair.second();
            if (bytes == null) {
                continue;
            }
            if (bytes.length < 8) {
                continue;
            }
            if (
                    (bytes[0] & 0xFF) != 0xCA ||
                            (bytes[1] & 0xFF) != 0xFE ||
                            (bytes[2] & 0xFF) != 0xBA ||
                            (bytes[3] & 0xFF) != 0xBE
            ) {
                continue;
            }
            byte b1 = bytes[6];
            byte b2 = bytes[7];
            int majorVersion = (b1 & 0xFF) << 8 | (b2 & 0xFF);
            int jdkVersion = majorVersion - 44;
            if (!list.contains(jdkVersion)) {
                list.add(jdkVersion);
            }
        }

        Collections.sort(list);
        return list;
    }

    public static byte[] readEntry(Path zipPath, String entry) throws IOException {
        Objects.requireNonNull(zipPath, "zipPath");
        Objects.requireNonNull(entry, "entry");

        return ZipNioUtils.process(zipPath, false, zipFs -> {
            Path path = zipFs.getPath(entry);
            try {
                return Files.readAllBytes(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static List<Pair<String, byte[]>> readClassList(Path jarPath) throws IOException {
        Objects.requireNonNull(jarPath, "jarPath must not be null");
        List<Path> classList = ZipFindNioUtils.getClassList(jarPath);
        List<String> entryList = classList.stream().map(Path::toString).toList();
        return readEntryList(jarPath, entryList);
    }

    public static List<Pair<String, byte[]>> readEntryList(Path zipPath, List<String> entryList) throws IOException {
        Objects.requireNonNull(zipPath, "zipPath");
        Objects.requireNonNull(entryList, "entryList must not be null");
        if (entryList.isEmpty()) {
            return Collections.emptyList();
        }

        return ZipNioUtils.process(zipPath, false, zipFs -> {
            List<Pair<String, byte[]>> list = new ArrayList<>();
            try {
                for (String entry : entryList) {
                    Path path = zipFs.getPath(entry);
                    if (Files.exists(path)) {
                        byte[] bytes = Files.readAllBytes(path);
                        list.add(new Pair<>(entry, bytes));
                    }
                    else {
                        list.add(new Pair<>(entry, null));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return list;
        });
    }

    public static void updateZipByMapOfPath(Path zipPath, Map<String, Path> classFileMap) throws IOException {

        updateZipByMap(zipPath, classFileMap, (internalPathInZip, externalPath) -> {
            try {
                Files.copy(externalPath, internalPathInZip, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void updateZipByMapOfByteArray(Path zipPath, Map<String, byte[]> classFileMap) throws IOException {

        updateZipByMap(zipPath, classFileMap, (zipEntryPath, bytes) -> {
            try {
                Files.write(zipEntryPath, bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public static <T> void updateZipByMap(Path jarPath, Map<String, T> map, BiConsumer<Path, T> biConsumer) throws IOException {

        ZipNioUtils.consume(jarPath, zipFs -> {
            try {
                for (Map.Entry<String, T> entry : map.entrySet()) {
                    Path internalPathInZip = zipFs.getPath(entry.getKey());
                    T value = entry.getValue();
                    if (!Files.exists(internalPathInZip)) {
                        Path parent = internalPathInZip.getParent();
                        Files.createDirectories(parent);
                    }
                    biConsumer.accept(internalPathInZip, value);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });
    }


}
