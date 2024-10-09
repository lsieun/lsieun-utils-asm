package lsieun.asm.utils;

import lsieun.asm.function.consumer.InsnInvokeConsumer;
import lsieun.asm.function.match.InsnInvokeMatch;
import lsieun.asm.function.match.MethodMatch;
import lsieun.asm.function.match.MethodMatchBuddy;
import lsieun.asm.function.match.TextMatchBuddy;
import lsieun.utils.io.file.FileContentUtils;
import lsieun.utils.io.resource.ResourceUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ClassFileModifyUtilsTest {
    @Test
    void testPrintMethod() throws IOException {
        String relativePath = "sample/HelloWorld.class";
        Path path = ResourceUtils.readFilePath(relativePath);
        byte[] bytes1 = FileContentUtils.readBytes(path);
        MethodMatch methodMatch = MethodMatchBuddy.byName(
                TextMatchBuddy.equals("test")
        );
        InsnInvokeMatch insnInvokeMatch = InsnInvokeMatch.ByReturn.METHOD;
        InsnInvokeConsumer insnInvokeConsumer = InsnInvokeConsumer.ThreePhase.builder()
                .withPreInvokeConsumer()
                .withOnInvokeConsumer(InsnInvokeConsumer.Default.INSTANCE)
                .withPostInvokeConsumer(InsnInvokeConsumer.Print.DUP_AND_PRINT_VALUE_ON_STACK);
        byte[] bytes2 = ClassFileModifyUtils.patchInsnInvoke(bytes1, methodMatch, insnInvokeMatch, insnInvokeConsumer);
        FileContentUtils.writeBytes(path, bytes2);
    }
}