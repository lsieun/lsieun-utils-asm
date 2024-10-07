package lsieun.asm.cst;

import org.objectweb.asm.Opcodes;

public final class MyConst {
    public static int API_VERSION;

    public static final String COLON = ":";

    static {
        API_VERSION = Opcodes.ASM9;
    }

    public static final String PRINT_STACK_FRAME_METHOD_NAME = "printStackFrame";
    public static final String PRINT_STACK_FRAME_METHOD_DESC = "(Ljava/lang/StackWalker$StackFrame;)V";
}
