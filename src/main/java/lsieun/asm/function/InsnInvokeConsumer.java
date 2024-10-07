package lsieun.asm.function;

import org.objectweb.asm.MethodVisitor;

public interface InsnInvokeConsumer {
    void accept(MethodVisitor mv, int opcode, String owner, String name, String descriptor, boolean isInterface);

    enum Default implements InsnInvokeConsumer {
        INSTANCE;

        @Override
        public void accept(MethodVisitor mv, int opcode, String owner, String name, String descriptor, boolean isInterface) {
            if (mv != null) {
                mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            }
        }
    }
}
