package lsieun.asm.adapter.find.opcode.ref;

import lsieun.asm.adapter.RegexAdapter;
import lsieun.asm.cst.Constant;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class FindRefMethod extends RegexAdapter {
    public final String targetInternalName;

    public String currentMethodName;
    public String currentMethodDesc;

    public FindRefMethod(ClassVisitor classVisitor, String targetInternalName, String[] includes, String[] excludes) {
        super(classVisitor, includes, excludes);
        this.targetInternalName = targetInternalName;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        currentMethodName = name;
        currentMethodDesc = descriptor;

        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        return new MethodOpcodeVisitor(mv);
    }

    class MethodOpcodeVisitor extends MethodVisitor {

        public MethodOpcodeVisitor(MethodVisitor methodVisitor) {
            super(Constant.API_VERSION, methodVisitor);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            String name_desc = String.format("%s%s%s", name, Constant.NAME_DESCRIPTOR_SEPARATOR, descriptor);
            if (owner.equals(targetInternalName) && isAppropriate(name_desc)) {
                gotcha = true;
                getResult().add(currentMethodName, currentMethodDesc);
            }
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }
}
