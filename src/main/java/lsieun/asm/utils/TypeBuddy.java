package lsieun.asm.utils;

import org.objectweb.asm.Type;

public class TypeBuddy {
    public static Type toArray(Type t, int dimension) {
        String arrayDescriptor = "[".repeat(dimension) + t.getDescriptor();
        return Type.getType(arrayDescriptor);
    }
}
