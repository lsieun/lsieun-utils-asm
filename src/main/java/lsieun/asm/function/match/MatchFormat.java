package lsieun.asm.function.match;

import lsieun.asm.description.ByteCodeElementType;

public class MatchFormat {
    public static String format(MatchState state, ByteCodeElementType elementType, String msg) {
        return String.format("[%s %s] %s", state, elementType, msg);
    }

    public static String format(MatchState state, ByteCodeElementType elementType,
                                String owner, String name, String desc) {
        return String.format("[%s %s] %s::%s:%s", state, elementType, owner, name, desc);
    }
}
