package lsieun.asm.visitor;

import lsieun.asm.cst.Constant;
import lsieun.asm.utils.PrintUtils;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class PrintMethodAdapter extends MethodVisitor implements Opcodes {
    private static final String INTERNAL_NAME = PrintUtils.class.getName().replace('.', '/');

    public PrintMethodAdapter(MethodVisitor methodVisitor) {
        super(Constant.API_VERSION, methodVisitor);
    }

    protected void printMessage(String str) {
        super.visitLdcInsn(str);
        super.visitMethodInsn(INVOKESTATIC, INTERNAL_NAME, "printText", "(Ljava/lang/String;)V", false);
    }

    protected void printValueOnStack(String descriptor) {
        super.visitMethodInsn(INVOKESTATIC, INTERNAL_NAME, "printValueOnStack", descriptor, false);
    }

    protected void printStackTrace() {
        super.visitMethodInsn(INVOKESTATIC, INTERNAL_NAME, "printStackTrace", "()V", false);
    }

    protected void dup(Type t) {
        int size = t.getSize();
        if (size == 1) {
            super.visitInsn(DUP);
        }
        else if (size == 2) {
            super.visitInsn(DUP2);
        }
        else {
            // do nothing
        }
    }

    protected void printValueOnStack(Type t) {
        int sort = t.getSort();
        if (sort == 0) {
            super.visitLdcInsn("void");
            printValueOnStack("(Ljava/lang/Object;)V");
        }
        else if (sort >= Type.BOOLEAN && sort <= Type.DOUBLE) {
            String desc = t.getDescriptor();
            printValueOnStack("(" + desc + ")V");
        }
        else {
            printValueOnStack("(Ljava/lang/Object;)V");
        }
    }

    protected void dupAndPrintValueOnStack(Type t) {
        dup(t);
        printValueOnStack(t);
    }
}
