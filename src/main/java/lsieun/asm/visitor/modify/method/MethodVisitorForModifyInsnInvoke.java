package lsieun.asm.visitor.modify.method;

import lsieun.asm.function.InsnInvokeConsumer;
import lsieun.asm.function.InsnInvokeMatch;
import lsieun.asm.visitor.common.MethodVisitorForInsnInvokeMatch;
import org.objectweb.asm.MethodVisitor;

public class MethodVisitorForModifyInsnInvoke extends MethodVisitorForInsnInvokeMatch {
    private final InsnInvokeConsumer insnInvokeConsumer;

    protected MethodVisitorForModifyInsnInvoke(MethodVisitor methodVisitor,
                                               String currentType, String currentMethodName, String currentMethodDesc,
                                               InsnInvokeMatch insnInvokeMatch,
                                               InsnInvokeConsumer insnInvokeConsumer) {
        super(methodVisitor, currentType, currentMethodName, currentMethodDesc, insnInvokeMatch);
        this.insnInvokeConsumer = insnInvokeConsumer;
    }


    @Override
    protected void generateNewMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        insnInvokeConsumer.accept(mv, opcode, owner, name, descriptor, isInterface);
    }
}