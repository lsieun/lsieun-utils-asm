package lsieun.asm.adapter;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.*;

public class MethodReplaceAdapter extends RegexAdapter {
    private boolean keepOldMethod;

    public MethodReplaceAdapter(ClassVisitor classVisitor, String[] includes, String[] excludes) {
        this(classVisitor, includes, excludes, false);
    }

    public MethodReplaceAdapter(ClassVisitor classVisitor, String[] includes, String[] excludes, boolean keepOldMethod) {
        super(classVisitor, includes, excludes);
        this.keepOldMethod = keepOldMethod;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        // （1） 接口，不处理
        if (isInterface) {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }

        //（2）抽象方法，不处理
        boolean isAbstractMethod = (access & ACC_ABSTRACT) != 0;
        if (isAbstractMethod) {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }

        //（3）不符合正则表达式，不处理
        boolean isTargetMethod = isTargetMember(name, descriptor);
        if (!isTargetMethod) {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }

        //（4）开始处理
        String newName = getNewName(name);
        generateNewBody(access, name, descriptor, signature, exceptions);

        if (keepOldMethod) {
            return super.visitMethod(access, newName, descriptor, signature, exceptions);
        }
        else {
            return null;
        }

    }

    private final void generateNewBody(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
        mv.visitCode();
        generateNewBody(mv, access, name, descriptor, signature, exceptions);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    /**
     * 这里提供一种默认实现，SubClass要根据实际的情况来Override这个方法
     * @param mv
     * @param access
     * @param name
     * @param descriptor
     * @param signature
     * @param exceptions
     */
    protected void generateNewBody(MethodVisitor mv, int access, String name, String descriptor, String signature, String[] exceptions) {
        Type t = Type.getType(descriptor);
        Type returnType = t.getReturnType();

        if (returnType.getSort() == Type.VOID) {
            mv.visitInsn(RETURN);
        }
        else if (returnType.getSort() >= Type.BOOLEAN && returnType.getSort() <= Type.DOUBLE) {

            mv.visitInsn(returnType.getOpcode(ICONST_0));
            mv.visitInsn(returnType.getOpcode(IRETURN));
        }
        else {
            mv.visitInsn(ACONST_NULL);
            mv.visitInsn(ARETURN);
        }

    }

    public final String getNewName(String name) {
        return String.format("orig$%s%s", name, System.currentTimeMillis());
    }

}
