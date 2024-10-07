package lsieun.asm.visitor.patch;

import lsieun.asm.cst.MyConst;
import lsieun.asm.search.SearchItem;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.function.BiConsumer;

public class PatchByMethodInvokeVisitor extends ClassVisitor {
    private final String refClassName;
    private final String refMethodName;
    private final String refMethodDesc;

    private final BiConsumer<MethodVisitor, SearchItem> consumer;

    private String currentClassName;

    public PatchByMethodInvokeVisitor(ClassVisitor classVisitor,
                                      String refClassName, String refMethodName, String refMethodDesc,
                                      BiConsumer<MethodVisitor, SearchItem> consumer) {
        super(MyConst.API_VERSION, classVisitor);
        this.refClassName = refClassName;
        this.refMethodName = refMethodName;
        this.refMethodDesc = refMethodDesc;
        this.consumer = consumer;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.currentClassName = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if ((access & Opcodes.ACC_ABSTRACT) != 0) return mv;
        if ((access & Opcodes.ACC_NATIVE) != 0) return mv;
        return new PatchByMethodInvokeAdapter(mv, name, descriptor);
    }

    private class PatchByMethodInvokeAdapter extends MethodVisitor {
        private final String currentMethodName;
        private final String currentMethodDesc;

        protected PatchByMethodInvokeAdapter(MethodVisitor methodVisitor,
                                             String currentMethodName, String currentMethodDesc) {
            super(MyConst.API_VERSION, methodVisitor);
            this.currentMethodName = currentMethodName;
            this.currentMethodDesc = currentMethodDesc;
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            if (owner.equals(refClassName) && name.equals(refMethodName) && descriptor.endsWith(refMethodDesc)) {
                SearchItem searchItem = SearchItem.ofMethod(currentClassName, currentMethodName, currentMethodDesc);
                consumer.accept(this, searchItem);
            }
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }
}
