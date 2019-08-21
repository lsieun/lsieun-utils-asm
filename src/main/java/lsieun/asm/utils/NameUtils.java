package lsieun.asm.utils;

public class NameUtils {

    public static String getInternalName(String fqcn) {
        return fqcn.replace(".", "/");
    }

    public static String getJarItemName(String fqcn) {
        return String.format("%s.class", getInternalName(fqcn));
    }

    public static String getFQCN(String internalName) {
        return internalName.replace("/", ".");
    }

}
