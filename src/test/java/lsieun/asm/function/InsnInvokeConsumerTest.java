package lsieun.asm.function;

import lsieun.asm.function.consumer.InsnInvokeConsumer;
import lsieun.asm.function.match.InsnInvokeMatch;
import lsieun.asm.function.match.MethodMatch;
import lsieun.asm.function.match.TypeMatch;
import lsieun.asm.utils.ClassFileModifyUtils;
import lsieun.asm.utils.TypeNameUtils;
import lsieun.utils.io.file.FileContentUtils;
import lsieun.utils.io.resource.ResourceUtils;
import lsieun.utils.log.LogLevel;
import lsieun.utils.log.Logger;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.nio.file.Path;

class InsnInvokeConsumerTest {
    @Test
    void testPopFromStack() throws IOException {
        Logger.CURRENT_LEVEL = LogLevel.DEBUG;
        Class<?> clazz = HelloWorldForPop.class;
        byte[] bytes = ResourceUtils.readClassBytes(clazz);
        byte[] newBytes = ClassFileModifyUtils.patchInsnInvoke(bytes,
                MethodMatch.Bool.TRUE,
                InsnInvokeMatch.byReturnType(TypeMatch.byType(void.class)),
                InsnInvokeConsumer.Common.POP_FROM_STACK
        );
        Type t = Type.getType(clazz);
        Path path = Path.of(".", "target", "classes", TypeNameUtils.toJarEntryName(t));
        FileContentUtils.writeBytes(path, newBytes);
    }

    private static class HelloWorldForPop {
        void test() {
            System.out.println("Hello World");
        }
    }


}