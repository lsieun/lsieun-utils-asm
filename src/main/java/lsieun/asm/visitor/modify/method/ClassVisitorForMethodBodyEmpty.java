package lsieun.asm.visitor.modify.method;

import lsieun.asm.function.MethodMatch;
import lsieun.asm.visitor.common.ClassVisitorForMethodMatch;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ClassVisitorForMethodBodyEmpty extends ClassVisitorForMethodMatch {
    public ClassVisitorForMethodBodyEmpty(ClassVisitor classVisitor, MethodMatch match) {
        super(classVisitor, match);
    }

    @Override
    protected MethodVisitor getNewMethodVisitor(MethodVisitor mv,
                                                int methodAccess, String methodName, String methodDesc,
                                                String signature, String[] exceptions) {
        // (1) method argument types and return type
        Type t = Type.getType(methodDesc);
        Type[] argumentTypes = t.getArgumentTypes();
        Type returnType = t.getReturnType();


        // (2) compute the size of local variable and operand stack
        boolean isStaticMethod = ((methodAccess & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC);
        int localSize = isStaticMethod ? 0 : 1;
        for (Type argType : argumentTypes) {
            localSize += argType.getSize();
        }
        int stackSize = returnType.getSize();


        // (3) method body
        mv.visitCode();
        if (returnType.getSort() == Type.VOID) {
            mv.visitInsn(Opcodes.RETURN);
        }
        else if (returnType.getSort() >= Type.BOOLEAN && returnType.getSort() <= Type.DOUBLE) {
            mv.visitInsn(returnType.getOpcode(Opcodes.ICONST_1));
            mv.visitInsn(returnType.getOpcode(Opcodes.IRETURN));
        }
        else {
            mv.visitInsn(Opcodes.ACONST_NULL);
            mv.visitInsn(Opcodes.ARETURN);
        }
        mv.visitMaxs(stackSize, localSize);
        mv.visitEnd();
        return null;
    }
}
