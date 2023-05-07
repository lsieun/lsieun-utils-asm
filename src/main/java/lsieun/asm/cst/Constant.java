package lsieun.asm.cst;

import static org.objectweb.asm.Opcodes.ASM9;

public final class Constant {
    public static final int API_VERSION;

    public static final String COLON = ":";

    static {
        API_VERSION = ASM9;
    }
}
