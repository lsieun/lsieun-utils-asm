package lsieun.asm.utils;

import lsieun.asm.function.match.InsnInvokeMatch;
import lsieun.asm.function.match.MatchAsmUtils;
import lsieun.asm.function.match.MethodMatch;
import lsieun.asm.search.SearchItem;
import lsieun.utils.io.resource.ResourceUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClassFileFindUtilsTest {
    @Test
    void findMethodBySystemExit() throws IOException {
        byte[] bytes = ResourceUtils.readClassBytes(HelloWorldForSystemExit.class);
        List<SearchItem> itemList = ClassFileFindUtils.findMethodByInsnInvoke(
                bytes,
                MethodMatch.Bool.TRUE,
                InsnInvokeMatch.ByMethodInsn.SYSTEM_EXIT);
        itemList.forEach(System.out::println);
        assertNotEquals(0, itemList.size());
    }

    class HelloWorldForSystemExit {
        public void aaa() {
            System.out.println("aaa");
        }

        public void bbb() {
            System.out.println("bbb");
            System.exit(0);
        }

        public void ccc() {
            System.out.println("ccc");
        }
    }
}