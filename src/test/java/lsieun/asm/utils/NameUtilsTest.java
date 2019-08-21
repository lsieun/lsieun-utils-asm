package lsieun.asm.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class NameUtilsTest {

    @Test
    public void getInternalName() {
        String fqcn = "com.example.HelloWorld";
        assertEquals("com/example/HelloWorld", NameUtils.getInternalName(fqcn));
    }

    @Test
    public void getJarItemName() {
        String fqcn = "com.example.HelloWorld";
        assertEquals("com/example/HelloWorld.class", NameUtils.getJarItemName(fqcn));
    }

    @Test
    public void getFQCN() {
        String internalName = "com/example/HelloWorld";
        assertEquals("com.example.HelloWorld", NameUtils.getFQCN(internalName));
    }

}