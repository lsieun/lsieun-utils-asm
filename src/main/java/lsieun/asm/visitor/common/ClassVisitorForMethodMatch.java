package lsieun.asm.visitor.common;

import lsieun.asm.cst.MyConst;
import lsieun.asm.function.MethodMatch;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * <pre>
 *                                                     ┌─── onVisitMethodEnter()
 *                                                     │
 * ClassVisitorForMethodMatch ───┼─── visitMethod() ───┼─── MethodMatch.test()
 *                                                     │
 *                                                     └─── getNewMethodVisitor()
 * </pre>
 */
public abstract class ClassVisitorForMethodMatch extends ClassVisitor implements Opcodes {
    protected int version;
    protected String currentOwner;
    private final MethodMatch methodMatch;

    protected ClassVisitorForMethodMatch(ClassVisitor classVisitor, MethodMatch methodMatch) {
        super(MyConst.API_VERSION, classVisitor);
        this.methodMatch = methodMatch;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.version = version;
        this.currentOwner = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        // 可以做一些辅助性的工作，例如『判断方法是否存在』
        onVisitMethodEnter(version, currentOwner, access, name, descriptor, signature, exceptions);

        // match: find, modify
        boolean flag = methodMatch.test(version, currentOwner, access, name, descriptor, signature, exceptions);

        // mv
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

        // (1) mv is null
        if (mv == null) {
            return mv;
        }

        // (2) abstract or native, do not process
        boolean isAbstract = (access & Opcodes.ACC_ABSTRACT) != 0;
        boolean isNative = (access & Opcodes.ACC_NATIVE) != 0;
        if (isAbstract || isNative) {
            return mv;
        }

        // (3) <init> or <clinit>, do not process
        if (name.equals("<init>") || name.equals("<clinit>")) {
            return mv;
        }

        // (4) if flag is true, then process
        if (flag) {
            String line = String.format("---> %s::%s:%s", currentOwner, name, descriptor);
            System.out.println(line);
            mv = getNewMethodVisitor(mv, access, name, descriptor, signature, exceptions);
        }

        return mv;
    }

    /**
     * 可以做一些辅助性的工作，例如『判断方法是否存在』
     */
    protected void onVisitMethodEnter(int version, String owner,
                                      int methodAccess, String methodName, String methodDesc,
                                      String signature, String[] exceptions) {
    }

    protected abstract MethodVisitor getNewMethodVisitor(MethodVisitor mv,
                                                         int methodAccess, String methodName, String methodDesc,
                                                         String signature, String[] exceptions);
}
