package lsieun.utils.io.resource;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassVisitor;

import javax.swing.tree.TreeModel;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ResourceUtilsTest {
    @Test
    void testReadClassBytes() throws IOException {
        Class<?>[] clazzArray = {
                Object.class,
                Map.Entry.class,
                TreeModel.class,
                ClassVisitor.class,
                ResourceUtils.class,
        };

        for (Class<?> clazz : clazzArray) {
            byte[] bytes = ResourceUtils.readClassBytes(clazz);
            assertNotNull(bytes);
        }
    }
}