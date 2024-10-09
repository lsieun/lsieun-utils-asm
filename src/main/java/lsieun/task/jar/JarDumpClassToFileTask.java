package lsieun.task.jar;

import lsieun.annotation.method.MethodParamExample;
import lsieun.asm.function.match.MethodMatch;
import lsieun.asm.utils.ClassFileModifyUtils;
import lsieun.asm.visitor.modify.method.MethodBodyInfoType;
import lsieun.utils.archive.ZipContentNioUtils;
import lsieun.utils.io.file.FileContentUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.Set;

public class JarDumpClassToFileTask {
    public static void dumpOrigin(Path jarPath,
                                  @MethodParamExample({"com/abc/Xyz.class"}) String entry,
                                  Path targetDirPath) throws IOException {
        byte[] bytes = ZipContentNioUtils.readEntryBytes(jarPath, entry);
        Path filepath = targetDirPath.resolve(entry);
        FileContentUtils.writeBytes(filepath, bytes);
    }

    public static void dumpAndAddStackTrace(Path jarPath,
                                            @MethodParamExample({"com/abc/Xyz.class"}) String entry,
                                            MethodMatch methodMatch,
                                            Path targetDirPath) throws IOException {
        EnumSet<MethodBodyInfoType> options = EnumSet.of(
                MethodBodyInfoType.ENTER,
                MethodBodyInfoType.PARAMETER_VALUES,
                MethodBodyInfoType.STACK_TRACE,
                MethodBodyInfoType.EXIT
        );
        dumpAndAddStackTrace(jarPath, entry, methodMatch, options, targetDirPath);
    }

    public static void dumpAndAddStackTrace(Path jarPath,
                                            @MethodParamExample({"com/abc/Xyz.class"}) String entry,
                                            MethodMatch methodMatch,
                                            Set<MethodBodyInfoType> options,
                                            Path targetDirPath) throws IOException {
        byte[] bytes = ZipContentNioUtils.readEntryBytes(jarPath, entry);
        byte[] newBytes = ClassFileModifyUtils.printMethodInfo(bytes, methodMatch, options);
        Path filepath = targetDirPath.resolve(entry);
        FileContentUtils.writeBytes(filepath, newBytes);
    }
}
