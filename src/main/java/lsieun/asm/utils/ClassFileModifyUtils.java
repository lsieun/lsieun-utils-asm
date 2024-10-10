package lsieun.asm.utils;

import lsieun.asm.function.consumer.InsnInvokeConsumer;
import lsieun.asm.function.match.InsnInvokeMatch;
import lsieun.asm.function.match.MethodMatch;
import lsieun.asm.visitor.modify.method.ClassVisitorForMethodBodyEmpty;
import lsieun.asm.visitor.modify.method.ClassVisitorForMethodBodyInfo;
import lsieun.asm.visitor.modify.method.ClassVisitorForModifyInsnInvoke;
import lsieun.asm.visitor.modify.method.MethodBodyInfoType;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.util.Set;

@SuppressWarnings("UnnecessaryLocalVariable")
public class ClassFileModifyUtils {
    public static byte[] patchInsnInvoke(byte[] bytes, MethodMatch methodMatch,
                                         InsnInvokeMatch insnInvokeMatch, InsnInvokeConsumer insnInvokeConsumer) {
        //（1）构建ClassReader
        ClassReader cr = new ClassReader(bytes);

        //（2）构建ClassWriter
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        //（3）串连ClassVisitor
        ClassVisitor cv = new ClassVisitorForModifyInsnInvoke(cw, methodMatch, insnInvokeMatch, insnInvokeConsumer);

        //（4）结合ClassReader和ClassVisitor
        int parsingOptions = ClassReader.EXPAND_FRAMES;
        cr.accept(cv, parsingOptions);

        //（5）生成byte[]
        byte[] newBytes = cw.toByteArray();
        return newBytes;
    }

    public static byte[] emptyMethodBody(byte[] bytes, MethodMatch match) {
        //（1）构建ClassReader
        ClassReader cr = new ClassReader(bytes);

        //（2）构建ClassWriter
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        //（3）串连ClassVisitor
        ClassVisitor cv = new ClassVisitorForMethodBodyEmpty(cw, match);

        //（4）结合ClassReader和ClassVisitor
        int parsingOptions = ClassReader.EXPAND_FRAMES;
        cr.accept(cv, parsingOptions);

        //（5）生成byte[]
        byte[] newBytes = cw.toByteArray();
        return newBytes;
    }

    public static byte[] printMethodInfo(byte[] bytes, MethodMatch match, Set<MethodBodyInfoType> options) {
        //（1）构建ClassReader
        ClassReader cr = new ClassReader(bytes);

        //（2）构建ClassWriter
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        //（3）串连ClassVisitor
        ClassVisitor cv = new ClassVisitorForMethodBodyInfo(cw, match, options);

        //（4）结合ClassReader和ClassVisitor
        int parsingOptions = ClassReader.EXPAND_FRAMES;
        cr.accept(cv, parsingOptions);

        //（5）生成byte[]
        byte[] newBytes = cw.toByteArray();
        return newBytes;
    }
}
