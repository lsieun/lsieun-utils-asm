package lsieun.asm.utils;

import org.objectweb.asm.Type;

public class TypeNameUtils {
    public static String toClassName(String text) {
        return parse(text).getClassName();
    }

    public static String toInternalName(String text) {
        return parse(text).getInternalName();
    }

    public static String toDescriptor(String text) {
        return parse(text).getDescriptor();
    }

    public static String toJarEntryName(String text) {
        String internalName = toInternalName(text);
        return String.format("%s.class", internalName);
    }

    public static Type parse(String text) {
        text = text.replace('.', '/');
        Type t;
        if (text.contains("(") && text.contains(")")) {
            Type methodType = Type.getMethodType(text);
            t = methodType.getReturnType();
        }
        else if (text.startsWith("L") && text.endsWith(";")) {
            t = Type.getType(text);
        }
        else {
            t = Type.getObjectType(text);
        }
        return t;
    }
}
