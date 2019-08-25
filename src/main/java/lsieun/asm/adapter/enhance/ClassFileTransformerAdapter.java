package lsieun.asm.adapter.enhance;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

import lsieun.asm.adapter.MethodEnhancedAdapter;
import lsieun.asm.cst.Constant;

public class ClassFileTransformerAdapter extends MethodEnhancedAdapter {
    private String containerName;

    public ClassFileTransformerAdapter(ClassVisitor classVisitor, String containerName) {
        super(classVisitor, new String[] {
                "^transform:\\(Ljava/lang/ClassLoader;Ljava/lang/String;Ljava/lang/Class;Ljava/security/ProtectionDomain;\\[B\\)\\[B$",
        });
        this.containerName = containerName;
    }

    @Override
    protected MethodVisitor enhanceMethodVisitor(MethodVisitor mv, int access, String name, String descriptor, String signature, String[] exceptions) {
        return new ClassFileTransformerVisitor(mv, access, name, descriptor);
    }

    class ClassFileTransformerVisitor extends AdviceAdapter {

        public ClassFileTransformerVisitor(MethodVisitor methodVisitor, int access, String name, String descriptor) {
            super(Constant.API_VERSION, methodVisitor, access, name, descriptor);
        }

        @Override
        protected void onMethodEnter() {
            if (mv != null) {
                mv.visitTypeInsn(NEW, containerName);
                mv.visitInsn(DUP);
                mv.visitVarInsn(ALOAD, 2);
                mv.visitVarInsn(ALOAD, 5);
                mv.visitMethodInsn(INVOKESPECIAL, containerName, "<init>", "(Ljava/lang/String;[B)V", false);
                mv.visitMethodInsn(INVOKESTATIC, containerName, "start", "(L" + containerName + ";)V", false);
            }
        }

        @Override
        protected void onMethodExit(int opcode) {
            if (mv != null) {
                if (opcode == ARETURN) {
                    mv.visitInsn(DUP);
                    mv.visitVarInsn(ALOAD, 2);
                    mv.visitMethodInsn(INVOKESTATIC, containerName, "stop", "([BLjava/lang/String;)V", false);
                }
            }
        }
    }
}
