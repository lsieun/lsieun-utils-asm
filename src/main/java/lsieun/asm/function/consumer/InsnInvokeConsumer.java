package lsieun.asm.function.consumer;

import lsieun.asm.function.match.MethodMatch;
import lsieun.asm.function.match.InsnInvokeMatch;
import lsieun.asm.utils.CodeSegmentUtils;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @see lsieun.asm.utils.ClassFileModifyUtils#patchInsnInvoke(byte[], MethodMatch, InsnInvokeMatch, InsnInvokeConsumer)
 */
public interface InsnInvokeConsumer {
    void accept(MethodVisitor mv,
                String currentType, String currentMethodName, String currentMethodDesc,
                int opcode, String owner, String name, String descriptor, boolean isInterface);

    enum Default implements InsnInvokeConsumer {
        INSTANCE;

        @Override
        public void accept(MethodVisitor mv,
                           String currentType, String currentMethodName, String currentMethodDesc,
                           int opcode, String owner, String name, String descriptor, boolean isInterface) {
            if (mv != null) {
                mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            }
        }
    }

    enum Common implements InsnInvokeConsumer {
        POP_FROM_STACK {
            @Override
            public void accept(MethodVisitor mv,
                               String currentType, String currentMethodName, String currentMethodDesc,
                               int opcode, String owner, String name, String descriptor, boolean isInterface) {
                if (mv != null) {
                    if (opcode == Opcodes.INVOKESPECIAL && "<init>".equals(name)) {
                        mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                    }
                    else {
                        CodeSegmentUtils.popByMethodDesc(mv, opcode == Opcodes.INVOKESTATIC, descriptor);
                    }
                }
            }
        },
        STACK_TRACE_BEFORE {
            @Override
            public void accept(MethodVisitor mv,
                               String currentType, String currentMethodName, String currentMethodDesc,
                               int opcode, String owner, String name, String descriptor, boolean isInterface) {
                if (mv != null) {
                    String msg = String.format("METHOD %s %s:%s",
                            currentType,
                            currentMethodName,
                            currentMethodDesc);
                    CodeSegmentUtils.printStackTrace(mv, msg);
                    mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                }

            }
        },
        STACK_TRACE_AFTER {
            @Override
            public void accept(MethodVisitor mv,
                               String currentType, String currentMethodName, String currentMethodDesc,
                               int opcode, String owner, String name, String descriptor, boolean isInterface) {
                if (mv != null) {
                    mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                    String msg = String.format("METHOD %s %s:%s",
                            currentType,
                            currentMethodName,
                            currentMethodDesc);
                    CodeSegmentUtils.printStackTrace(mv, msg);
                }

            }
        },
        ;
    }
}
