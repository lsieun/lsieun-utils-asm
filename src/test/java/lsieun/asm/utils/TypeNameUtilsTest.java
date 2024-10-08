package lsieun.asm.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypeNameUtilsTest {
    @Test
    void testToClassName() {
        String text = "com/jetbrains/b/Q/B";
        String className = TypeNameUtils.toClassName(text);
        System.out.println(className);
    }
}