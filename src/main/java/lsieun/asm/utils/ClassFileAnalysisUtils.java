package lsieun.asm.utils;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import java.util.function.Supplier;

/**
 * @see StdAnalysis
 */
public class ClassFileAnalysisUtils {
    public static void analysis(byte[] bytes, Supplier<ClassVisitor> supplier) {
        //（1）构建ClassReader
        ClassReader cr = new ClassReader(bytes);

        //（2）构建ClassVisitor
        ClassVisitor cv = supplier.get();

        //（3）结合ClassReader和ClassVisitor
        int parsingOptions = ClassReader.EXPAND_FRAMES;
        cr.accept(cv, parsingOptions);
    }
}
