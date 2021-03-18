package lsieun.asm.utils;

public class NameUtils {
    public static String toInternalName(String fully_qualified_class_name) {
        return fully_qualified_class_name.replace('.', '/');
    }

    public static String toJarItemName(String internalName) {
        return String.format("%s.class", internalName);
    }

    public static String toFullyQualifiedClassName(String internalName) {
        return internalName.replace('/', '.');
    }
}
