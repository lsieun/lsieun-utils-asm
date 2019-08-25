package lsieun.asm.utils;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class OpcodeUtils {
    private static final String OPCODE_INSTRUCTION_FORMAT = "%-14s%-6s//%s";

    public static void visitVarInsn(MethodVisitor mv, int opcode, int index) {
        String opcode_name = OpcodeConst.getOpcodeName(opcode);
        String opcode_arg = String.valueOf(index);

        Type t = null;
        if (opcode == ILOAD || opcode == ISTORE) {
            t = Type.INT_TYPE;
        } else if (opcode == LLOAD || opcode == LSTORE) {
            t = Type.LONG_TYPE;
        } else if (opcode == FLOAD || opcode == FSTORE) {
            t = Type.FLOAT_TYPE;
        } else if (opcode == DLOAD || opcode == DSTORE) {
            t = Type.DOUBLE_TYPE;
        } else if (opcode == ALOAD || opcode == ASTORE) {
            t = Type.getType(Object.class);
        }

        CodeUtils.dupStackValue(mv, t);
        CodeUtils.printStackObjectValue(mv, OPCODE_INSTRUCTION_FORMAT, new String[]{opcode_name, opcode_arg});
    }

    public static void printFieldValue(MethodVisitor mv, String descriptor) {
        Type t = Type.getType(descriptor);
        printType(mv, t);
    }

    public static void printMethodReturnValue(MethodVisitor mv, String descriptor) {
        Type t = Type.getReturnType(descriptor);
        printType(mv, t);
    }

    public static void printType(MethodVisitor mv, Type t) {
        if (t.getSort() == Type.VOID) {
            CodeUtils.printMessage(mv, "method OK");
            return;
        } else if (t.getSort() >= Type.BOOLEAN && t.getSort() <= Type.OBJECT) {
            String format = "Value(%s): %s";
            String desc = t.getDescriptor();
            CodeUtils.dupStackValue(mv, t);
            CodeUtils.printStackObjectValue(mv, format, new String[]{desc});
        } else {
            throw new RuntimeException("Unknown Type: " + t);
        }
    }

    /**
     * LADD, FADD, DADD, ISUB, LSUB, FSUB, DSUB, IMUL, LMUL, FMUL, DMUL, IDIV, LDIV,
     * *     FDIV, DDIV, IREM, LREM, FREM, DREM, INEG, LNEG, FNEG, DNEG, ISHL, LSHL, ISHR, LSHR, IUSHR,
     * *     LUSHR, IAND, LAND, IOR, LOR, IXOR, LXOR
     *
     * @param mv
     * @param opcode
     */
    public static void printOpcode(MethodVisitor mv, int opcode) {
        String opcode_name = OpcodeConst.getOpcodeName(opcode);
        CodeUtils.printMessage(mv, opcode_name);
    }

    public static void printOpcodeInstruction(MethodVisitor mv, int opcode, String opcode_arg, String more_info) {
        String opcode_name = OpcodeConst.getOpcodeName(opcode);

        if (OpcodeConst.getNoOfOperands(opcode) == 0) {
            opcode_arg = "";
        }

        CodeUtils.printMessage(mv, OPCODE_INSTRUCTION_FORMAT, new String[]{opcode_name, opcode_arg, more_info});
    }

}
