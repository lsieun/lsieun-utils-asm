package lsieun.asm.visitor.modify.method;

import lsieun.asm.function.InsnInvokeConsumer;
import lsieun.asm.function.InsnInvokeMatch;
import lsieun.asm.function.MethodMatch;
import lsieun.asm.visitor.common.ClassVisitorForMethodMatch;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class ClassVisitorForModifyInsnInvoke extends ClassVisitorForMethodMatch {
    private final InsnInvokeMatch insnInvokeMatch;
    private final InsnInvokeConsumer insnInvokeConsumer;

    public ClassVisitorForModifyInsnInvoke(ClassVisitor classVisitor,
                                           MethodMatch methodMatch,
                                           InsnInvokeMatch insnInvokeMatch,
                                           InsnInvokeConsumer insnInvokeConsumer) {
        super(classVisitor, methodMatch);
        this.insnInvokeMatch = insnInvokeMatch;
        this.insnInvokeConsumer = insnInvokeConsumer;
    }

    @Override
    protected MethodVisitor getNewMethodVisitor(MethodVisitor mv,
                                                int methodAccess, String methodName, String methodDesc,
                                                String signature, String[] exceptions) {
        return new MethodVisitorForModifyInsnInvoke(mv,
                currentOwner, methodName, methodDesc,
                insnInvokeMatch, insnInvokeConsumer);
    }
}
