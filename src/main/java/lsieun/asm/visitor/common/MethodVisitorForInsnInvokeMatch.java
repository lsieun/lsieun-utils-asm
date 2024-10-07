package lsieun.asm.visitor.common;

import lsieun.asm.cst.MyConst;
import lsieun.asm.function.InsnInvokeMatch;
import org.objectweb.asm.MethodVisitor;

public abstract class MethodVisitorForInsnInvokeMatch extends MethodVisitor {
    protected final String currentType;
    protected final String currentMethodName;
    protected final String currentMethodDesc;
    private final InsnInvokeMatch insnInvokeMatch;

    protected MethodVisitorForInsnInvokeMatch(MethodVisitor methodVisitor,
                                              String currentType, String currentMethodName, String currentMethodDesc,
                                              InsnInvokeMatch insnInvokeMatch) {
        super(MyConst.API_VERSION, methodVisitor);
        this.currentType = currentType;
        this.currentMethodName = currentMethodName;
        this.currentMethodDesc = currentMethodDesc;
        this.insnInvokeMatch = insnInvokeMatch;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        boolean flag = insnInvokeMatch.test(opcode, owner, name, descriptor);
        if (flag) {
            generateNewMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
        else {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }

    protected abstract void generateNewMethodInsn(int opcode, String owner,
                                                  String name, String descriptor, boolean isInterface);
}
