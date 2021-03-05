package lsieun.asm.visitor;

import lsieun.asm.cst.Constant;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class PrintMethodOpcodeVisitor extends ClassVisitor {
    private final String methodName;
    private final String methodDesc;

    public PrintMethodOpcodeVisitor(ClassVisitor classVisitor, String methodName, String methodDesc) {
        super(Constant.API_VERSION, classVisitor);
        this.methodName = methodName;
        this.methodDesc = methodDesc;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (mv != null && name.equals(methodName) && descriptor.equals(methodDesc)) {
            mv = new PrintMethodAdapter(mv);
        }
        return mv;
    }
}
