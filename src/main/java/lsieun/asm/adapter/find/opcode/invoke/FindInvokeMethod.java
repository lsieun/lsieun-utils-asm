package lsieun.asm.adapter.find.opcode.invoke;

import lsieun.asm.adapter.RegexAdapter;
import lsieun.asm.adapter.find.Result;
import lsieun.asm.cst.Constant;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class FindInvokeMethod extends RegexAdapter {
    public FindInvokeMethod(ClassVisitor classVisitor, String[] includes, String[] excludes) {
        super(classVisitor, includes, excludes);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        boolean flag = isTargetMember(name, descriptor);
        if (flag) {
            gotcha = true;
            return new MethodOpcodeVisitor(mv);
        }
        return mv;
    }

    class MethodOpcodeVisitor extends MethodVisitor {

        public MethodOpcodeVisitor(MethodVisitor methodVisitor) {
            super(Constant.API_VERSION, methodVisitor);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            Result r = new Result(owner);
            r.add(name, descriptor);
            resultList.add(r);

            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }
}
