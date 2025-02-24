package lsieun.utils.match.archive;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

@FunctionalInterface
public interface ZipFileSystemMatch {
    boolean test(FileSystem zipFileSystem);

//    enum Bool implements ZipMatch {
//        TRUE {
//            @Override
//            public boolean test(FileSystem zipFileSystem) {
//                return true;
//            }
//        },
//        FALSE {
//            @Override
//            public boolean test(FileSystem zipFileSystem) {
//                return false;
//            }
//        };
//    }

//    class And implements ZipMatch {
//        private final ZipMatch[] matches;
//
//        private And(ZipMatch... matches) {
//            this.matches = matches;
//        }
//
//        @Override
//        public boolean test(FileSystem zipFileSystem) {
//            for (ZipMatch match : matches) {
//                if (!match.test(zipFileSystem)) {
//                    return false;
//                }
//            }
//            return true;
//        }
//
//        public static And of(ZipMatch... matches) {
//            return new And(matches);
//        }
//    }

//    class Or implements ZipMatch {
//        private final ZipMatch[] matches;
//
//        private Or(ZipMatch... matches) {
//            this.matches = matches;
//        }
//
//        @Override
//        public boolean test(FileSystem zipFileSystem) {
//            for (ZipMatch match : matches) {
//                if (match.test(zipFileSystem)) {
//                    return true;
//                }
//            }
//            return false;
//        }
//
//        public static Or of(ZipMatch... matches) {
//            return new Or(matches);
//        }
//    }

    // region static methods
    static ZipFileSystemMatch exists(String entry) {
        return zipFs -> {
            Path path = zipFs.getPath(entry);
            return Files.exists(path);
        };
    }

    static ZipFileSystemMatch existsByClassName(String className) {
        String entry = className.replace('.', '/') + ".class";
        return exists(entry);
    }

    static ZipFileSystemMatch existsByClass(Class<?> clazz) {
        String entry = clazz.getTypeName().replace('.', '/') + ".class";
        return exists(entry);
    }

    static ZipFileSystemMatch byZipEntry(ZipEntryMatch match) {
        return zipFs -> {
            Path dirPath = zipFs.getPath("/");
            BiPredicate<Path, BasicFileAttributes> predicate = (path, attr) -> true;

            try (Stream<Path> stream = Files.find(dirPath, Integer.MAX_VALUE, predicate)) {
                List<String> list = stream
                        .map(Path::toString)
                        .sorted()
                        .toList();
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    String entry = list.get(i);
                    if (match.test(zipFs, entry)) {
                        return true;
                    }
                }
                return false;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
    // endregion
}
