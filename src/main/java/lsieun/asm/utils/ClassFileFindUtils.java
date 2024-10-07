package lsieun.asm.utils;

import lsieun.asm.function.InsnInvokeMatch;
import lsieun.asm.function.MethodMatch;
import lsieun.asm.search.SearchItem;
import lsieun.asm.visitor.find.ClassVisitorForFindInsnInvoke;
import lsieun.asm.visitor.find.ClassVisitorForFindMethod;
import lsieun.utils.archive.ZipContentNioUtils;
import lsieun.utils.ds.pair.Pair;
import lsieun.utils.io.dir.DirNioUtils;
import lsieun.utils.log.Logger;
import lsieun.utils.log.LoggerFactory;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ClassFileFindUtils {
    private static final Logger logger = LoggerFactory.getLogger(ClassFileFindUtils.class);

    public static List<SearchItem> findMethod(byte[] bytes, MethodMatch methodMatch) {
        //（1）构建 ClassReader
        ClassReader cr = new ClassReader(bytes);

        //（2）构建 ClassVisitor
        ClassVisitorForFindMethod cv = new ClassVisitorForFindMethod(methodMatch);

        //（3）结合 ClassReader 和 ClassVisitor
        int parsingOptions = ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
        cr.accept(cv, parsingOptions);

        //（4）返回结果
        return cv.getResultList();
    }

    public static List<SearchItem> findInsnInvoke(byte[] bytes, MethodMatch methodMatch, InsnInvokeMatch insnInvokeMatch) {
        //（1）构建 ClassReader
        ClassReader cr = new ClassReader(bytes);

        //（2）构建 ClassVisitor
        ClassVisitorForFindInsnInvoke cv = new ClassVisitorForFindInsnInvoke(methodMatch, insnInvokeMatch);

        //（3）结合 ClassReader 和 ClassVisitor
        int parsingOptions = ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
        cr.accept(cv, parsingOptions);

        //（4）返回结果
        return cv.getResultList();
    }

    public static List<SearchItem> find(List<byte[]> byteArrayList, Function<byte[], List<SearchItem>> func) {
        List<SearchItem> resultList = new ArrayList<>();
        for (byte[] bytes : byteArrayList) {
            List<SearchItem> list = func.apply(bytes);
            resultList.addAll(list);
        }
        return resultList;
    }

    public static Pair<Path, List<SearchItem>> findInJar(Path jarPath,
                                                         Function<byte[], List<SearchItem>> func) throws IOException {
        logger.debug("jarPath: {0}", jarPath);
        List<Pair<String, byte[]>> pairList = ZipContentNioUtils.readClassList(jarPath);
        List<byte[]> byteArrayList = pairList.stream().map(Pair::second).toList();
        List<SearchItem> searchItemList = find(byteArrayList, func);
        return new Pair<>(jarPath, searchItemList);
    }

    public static List<Pair<Path, List<SearchItem>>> findInJarList(List<Path> jarPathList,
                                                                   Function<byte[], List<SearchItem>> func) throws IOException {
        List<Pair<Path, List<SearchItem>>> resultList = new ArrayList<>();
        for (Path path : jarPathList) {
            var pair = findInJar(path, func);
            resultList.add(pair);
        }
        return resultList;
    }

    public static List<Pair<Path, List<SearchItem>>> findInDir(Path dirPath, int maxDepth,
                                                               Function<byte[], List<SearchItem>> func) throws IOException {
        logger.debug("dirPath: {0}", dirPath);
        logger.debug("maxDepth: {0}", maxDepth);
        List<Path> jarPathList = DirNioUtils.findFileListInDirByExt(dirPath, maxDepth, ".jar");
        return findInJarList(jarPathList, func);
    }

    public static void print(List<Pair<Path, List<SearchItem>>> pairList) {
        for (Pair<Path, List<SearchItem>> pair : pairList) {
            print(pair);
        }
    }

    public static void print(Pair<Path, List<SearchItem>> pair) {
        List<SearchItem> list = pair.second();
        if (list.isEmpty()) {
            return;
        }
        System.out.println(pair.first());
        for (SearchItem item : list) {
            System.out.println("    " + item);
        }
    }
}
