package lsieun.asm.utils;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class CodeUtils {
    public static final String PRINT_Z = "(Z)V";
    public static final String PRINT_C = "(C)V";
    public static final String PRINT_I = "(I)V";
    public static final String PRINT_F = "(F)V";
    public static final String PRINT_J = "(J)V";
    public static final String PRINT_D = "(D)V";
    public static final String PRINT_STRING = "(Ljava/lang/String;)V";
    public static final String PRINT_ARRAY_C = "([C)V";
    public static final String PRINT_OBJECT = "(Ljava/lang/Object;)V";

    public static void printMessage(MethodVisitor mv, String message) {
        if (mv != null) {
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn(message);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", PRINT_STRING, false);
        }
    }

    public static void printMessage(MethodVisitor mv, String format, String[] args) {
        // 先验证参数，是否正确
        if (format == null || format.trim().isEmpty()) return;
        if (args == null || args.length < 1) return;

        // 再实现功能，要做什么
        if (mv != null) {
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn(format);

            mv.visitIntInsn(BIPUSH, args.length);
            mv.visitTypeInsn(ANEWARRAY, "java/lang/String");

            for (int i = 0; i < args.length; i++) {
                mv.visitInsn(DUP);
                mv.visitIntInsn(BIPUSH, i);
                mv.visitLdcInsn(args[i]);
                mv.visitInsn(AASTORE);
            }

            mv.visitMethodInsn(INVOKESTATIC, "java/lang/String", "format", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", PRINT_STRING, false);
        }
    }

    public static void printMethodName(MethodVisitor mv, String owner, String methodName, String methodDesc) {
        String format = "%s!%s:%s";
        String[] args = new String[] {owner, methodName, methodDesc};
        printMessage(mv, format, args);
    }

    public static void printMethodArguments(MethodVisitor mv, int access, String methodDesc) {
        if (mv != null) {
            Type[] argumentTypes = Type.getArgumentTypes(methodDesc);
            if (argumentTypes != null && argumentTypes.length > 0) {

                int index = 1;
                boolean isStaticMethod = (access & ACC_STATIC) != 0;
                if (isStaticMethod) {
                    index = 0;
                }

                for (int i = 0; i < argumentTypes.length; i++) {
                    Type t = argumentTypes[i];

                    if (t.getSort() == Type.BOOLEAN) { // boolean
                        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                        mv.visitVarInsn(ILOAD, index);
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Z)V", false);
                        index++;
                    }
                    else if (t.getSort() == Type.CHAR) { // char
                        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                        mv.visitVarInsn(ILOAD, index);
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(C)V", false);
                        index++;
                    }
                    else if (t.getSort() >= Type.BYTE && t.getSort() <= Type.INT) {
                        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                        mv.visitVarInsn(ILOAD, index);
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
                        index++;
                    }
                    else if (t.getSort() == Type.FLOAT) {
                        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                        mv.visitVarInsn(FLOAD, index);
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(F)V", false);
                        index++;
                    }
                    else if (t.getSort() == Type.LONG) {
                        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                        mv.visitVarInsn(LLOAD, index);
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(J)V", false);
                        index += 2;
                    }
                    else if (t.getSort() == Type.DOUBLE) {
                        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                        mv.visitVarInsn(DLOAD, index);
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(D)V", false);
                        index += 2;
                    }
                    else if (t.getSort() == Type.ARRAY) {
                        if (t.getDescriptor().equals("[C")) {
                            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                            mv.visitVarInsn(ALOAD, index);
                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", PRINT_ARRAY_C, false);
                            index++;
                        }
                        else {
                            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                            mv.visitVarInsn(ALOAD, index);
                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);
                            index++;
                        }

                    }
                    else if (t.getSort() == Type.OBJECT) {
                        if (t.getDescriptor().equals("Ljava/lang/String;")) {
                            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                            mv.visitVarInsn(ALOAD, index);
                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                            index++;
                        }
                        else {
                            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                            mv.visitVarInsn(ALOAD, index);
                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);
                            index++;
                        }
                    }
                    else {
                        throw new RuntimeException("Unknown Type: " + t);
                    }
                }
            }
        }
    }

    public static void printMethodReturnValue(MethodVisitor mv, int opcode, String methodDesc) {
        if (mv != null) {
            if (opcode >= IRETURN && opcode<=RETURN) {
                Type t = Type.getReturnType(methodDesc);

                if (t.getSort() == Type.VOID) {
                    printMessage(mv, "return VOID");
                }
                else if (t.getSort() >= Type.BOOLEAN && t.getSort() <= Type.DOUBLE) {
                    String format = "return %s: %s";
                    String desc = t.getDescriptor();

                    if (t.getSort() == Type.BOOLEAN) { // boolean
                        mv.visitInsn(DUP);
                        CodeBlock.boolean2Boolean(mv);
                    }
                    else if (t.getSort() == Type.CHAR) { // char
                        mv.visitInsn(DUP);
                        CodeBlock.char2Character(mv);
                    }
                    else if (t.getSort() == Type.BYTE) {
                        mv.visitInsn(DUP);
                        CodeBlock.byte2Byte(mv);
                    }
                    else if (t.getSort() == Type.SHORT) {
                        mv.visitInsn(DUP);
                        CodeBlock.short2Short(mv);
                    }
                    else if (t.getSort() == Type.INT) {
                        mv.visitInsn(DUP);
                        CodeBlock.int2Integer(mv);
                    }
                    else if (t.getSort() == Type.FLOAT) {
                        mv.visitInsn(DUP);
                        CodeBlock.float2Float(mv);
                    }
                    else if (t.getSort() == Type.LONG) {
                        mv.visitInsn(DUP2);
                        CodeBlock.long2Long(mv);
                    }
                    else if (t.getSort() == Type.DOUBLE) {
                        mv.visitInsn(DUP2);
                        CodeBlock.double2Double(mv);
                    }

                    CodeUtils.printStackObjectValue(mv, format, new Object[] {desc});
                }
                else if (t.getSort() == Type.ARRAY || t.getSort() == Type.OBJECT) {
                    String format = "return %s: %s";
                    String desc = t.getDescriptor();

                    mv.visitInsn(DUP);
                    CodeUtils.printStackObjectValue(mv, format, new Object[] {desc});
                }
                else {
                    throw new RuntimeException("Unknown Type: " + t);
                }

            }
            else if (opcode == ATHROW) {
                printMessage(mv, "=========AThrow=========" + methodDesc);
                //printStackTrace(mv);
            }

        }
    }

    public static void printStackObjectValue(MethodVisitor mv, String format, Object[] args) {
        // 先验证参数，是否正确
        if (format == null || format.trim().isEmpty()) return;
        if (args == null || args.length < 1) return;

        // 再实现功能，要做什么
        if (mv != null) {
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitInsn(SWAP);
            mv.visitLdcInsn(format);
            mv.visitInsn(SWAP);

            mv.visitIntInsn(BIPUSH, args.length + 1);
            mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");

            for (int i = 0; i < args.length; i++) {
                mv.visitInsn(DUP);
                mv.visitIntInsn(BIPUSH, i);
                mv.visitLdcInsn(args[i]);
                mv.visitInsn(AASTORE);
            }

            mv.visitInsn(DUP_X1);
            mv.visitInsn(SWAP);
            mv.visitIntInsn(BIPUSH, args.length);
            mv.visitInsn(SWAP);
            mv.visitInsn(AASTORE);


            mv.visitMethodInsn(INVOKESTATIC, "java/lang/String", "format", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", PRINT_STRING, false);
        }
    }

    public static void printStackTrace(MethodVisitor mv) {
        if (mv != null) {
            printMessage(mv, "=== === ===>>> Stack Trace");
            mv.visitTypeInsn(NEW, "java/lang/Exception");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Exception", "<init>", "()V", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "printStackTrace", "()V", false);
            printMessage(mv, "<<<=== === === Stack Trace");
        }
    }

}
