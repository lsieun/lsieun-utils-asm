package lsieun.asm.adapter.enhance;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import lsieun.asm.adapter.MethodEnhancedAdapter;
import lsieun.asm.cst.Constant;
import lsieun.asm.utils.OpcodeUtils;

public class MethodOpcodeAdapter extends MethodEnhancedAdapter {

    public MethodOpcodeAdapter(ClassVisitor classVisitor, String[] regex_array) {
        super(classVisitor, regex_array);
    }

    @Override
    protected MethodVisitor enhanceMethodVisitor(MethodVisitor mv, int access, String name, String descriptor, String signature, String[] exceptions) {
        return new MethodOpcodeVisitor(mv);
    }

    class MethodOpcodeVisitor extends MethodVisitor {

        public MethodOpcodeVisitor(MethodVisitor methodVisitor) {
            super(Constant.API_VERSION, methodVisitor);
        }

        // ILOAD, LLOAD, FLOAD, DLOAD, ALOAD,
        // ISTORE, LSTORE, FSTORE, DSTORE, ASTORE
        // or RET.
        @Override
        public void visitVarInsn(int opcode, int index) {
            if (mv != null) {
                if (opcode == ILOAD || opcode == LLOAD || opcode == FLOAD || opcode == DLOAD || opcode == ALOAD) {
                    mv.visitVarInsn(opcode, index);
                    OpcodeUtils.visitVarInsn(mv, opcode, index);
                }
                else if (opcode == ISTORE || opcode == LSTORE || opcode == FSTORE || opcode == DSTORE || opcode == ASTORE) {
                    OpcodeUtils.visitVarInsn(mv, opcode, index);
                    mv.visitVarInsn(opcode, index);
                }
                else {
                    if (opcode == RET) {
                        OpcodeUtils.printOpcodeInstruction(mv, opcode, String.valueOf(index), "");
                    }
                    mv.visitVarInsn(opcode, index);
                }
            }

        }

        @Override
        public void visitInsn(int opcode) {
            OpcodeUtils.printOpcode(mv ,opcode);
            super.visitInsn(opcode);
        }


        @Override
        public void visitIincInsn(int var, int increment) {
            String opcode_arg = String.format("%s %s", var, increment);
            OpcodeUtils.printOpcodeInstruction(mv, IINC, opcode_arg, "");
            super.visitIincInsn(var, increment);
        }

        @Override
        public void visitIntInsn(int opcode, int operand) {
            String opcode_arg = String.valueOf(operand);
            OpcodeUtils.printOpcodeInstruction(mv, opcode, opcode_arg, "");
            super.visitIntInsn(opcode, operand);
        }

        @Override
        public void visitJumpInsn(int opcode, Label label) {
            OpcodeUtils.printOpcode(mv ,opcode);
            super.visitJumpInsn(opcode, label);
        }

        @Override
        public void visitLdcInsn(Object value) {
            OpcodeUtils.printOpcodeInstruction(mv, LDC, "", String.valueOf(value));
            super.visitLdcInsn(value);
        }

        @Override
        public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
            OpcodeUtils.printOpcode(mv ,TABLESWITCH);
            super.visitTableSwitchInsn(min, max, dflt, labels);
        }

        @Override
        public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
            OpcodeUtils.printOpcode(mv ,LOOKUPSWITCH);
            super.visitLookupSwitchInsn(dflt, keys, labels);
        }

        @Override
        public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
            OpcodeUtils.printOpcodeInstruction(mv, MULTIANEWARRAY, String.valueOf(numDimensions), descriptor);
            super.visitMultiANewArrayInsn(descriptor, numDimensions);
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
            String more_info = String.format("%s.%s:%s", owner, name, descriptor);
            OpcodeUtils.printOpcodeInstruction(mv, opcode, "", more_info);
            super.visitFieldInsn(opcode, owner, name, descriptor);
            if (opcode == GETFIELD || opcode == GETSTATIC) {
                OpcodeUtils.printFieldValue(mv, descriptor);
            }
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            String more_info = String.format("%s.%s:%s", owner, name, descriptor);
            OpcodeUtils.printOpcodeInstruction(mv, opcode, "", more_info);
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            if (opcode == INVOKESTATIC) {
                OpcodeUtils.printMethodReturnValue(mv, descriptor);
            }
            if (opcode == INVOKESPECIAL) {
                OpcodeUtils.printMethodReturnValue(mv, descriptor);
            }
            if (opcode == INVOKEINTERFACE) {
                OpcodeUtils.printMethodReturnValue(mv, descriptor);
            }
            if (opcode == INVOKEVIRTUAL) {
                OpcodeUtils.printMethodReturnValue(mv, descriptor);
            }

        }

        @Override
        public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
            String more_info = String.format("%%s:%s", name, descriptor);
            OpcodeUtils.printOpcodeInstruction(mv, INVOKEDYNAMIC, "", more_info);
            super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
        }

        @Override
        public void visitTypeInsn(int opcode, String type) {
            OpcodeUtils.printOpcodeInstruction(mv, opcode, "", type);
            super.visitTypeInsn(opcode, type);
        }
    }
}
