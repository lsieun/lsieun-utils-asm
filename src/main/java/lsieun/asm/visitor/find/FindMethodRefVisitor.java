package lsieun.asm.visitor.find;

import lsieun.asm.cst.MyConst;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

public class FindMethodRefVisitor extends ClassVisitor {
    private final String refClassName;
    private final String refMethodName;
    private final String refMethodDesc;

    public final List<String> resultList = new ArrayList<>();
    private String currentClassName;


    public FindMethodRefVisitor(String refClassName, String refMethodName, String refMethodDesc) {
        super(MyConst.API_VERSION, null);
        this.refClassName = refClassName;
        this.refMethodName = refMethodName;
        this.refMethodDesc = refMethodDesc;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.currentClassName = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if ((access & Opcodes.ACC_ABSTRACT) != 0) return null;
        if ((access & Opcodes.ACC_NATIVE) != 0) return null;
        return new FindMethodRefAdapter(name, descriptor);
    }

    private class FindMethodRefAdapter extends MethodVisitor {
        private final String currentMethodName;
        private final String currentMethodDesc;

        public FindMethodRefAdapter(String currentMethodName, String currentMethodDesc) {
            super(MyConst.API_VERSION);
            this.currentMethodName = currentMethodName;
            this.currentMethodDesc = currentMethodDesc;
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            if (owner.equals(refClassName) && name.equals(refMethodName) && descriptor.endsWith(refMethodDesc)) {
                String item = String.format("%s.class %s:%s", currentClassName, currentMethodName, currentMethodDesc);
                if (!resultList.contains(item)) {
                    resultList.add(item);
                }
            }
        }
    }
}