package lsieun.asm.utils;

import lsieun.asm.search.SearchItem;
import lsieun.asm.visitor.patch.PatchByMethodInvokeVisitor;
import org.objectweb.asm.*;

import java.util.function.BiConsumer;

public class BytecodePatch {
    public static byte[] patchByMethodInvoke(byte[] bytes,
                                             String refClassName, String refMethodName, String refMethodDesc,
                                             BiConsumer<MethodVisitor, SearchItem> consumer) {
        //（1）构建ClassReader
        ClassReader cr = new ClassReader(bytes);

        //（2）构建ClassWriter
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        //（3）串连ClassVisitor
        int api = Opcodes.ASM9;
        ClassVisitor cv = new PatchByMethodInvokeVisitor(cw, refClassName, refMethodName, refMethodDesc, consumer);

        //（4）结合ClassReader和ClassVisitor
        int parsingOptions = ClassReader.EXPAND_FRAMES;
        cr.accept(cv, parsingOptions);

        //（5）生成byte[]
        return cw.toByteArray();
    }

    public static void printStackTrace(MethodVisitor mv, SearchItem searchItem) {
        String msg = String.format("METHOD %s %s:%s",
                searchItem.internalName,
                searchItem.name,
                searchItem.descriptor);
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/Exception");
        mv.visitInsn(Opcodes.DUP);
        mv.visitLdcInsn(msg);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Exception", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Exception", "printStackTrace", "()V", false);
    }
}
