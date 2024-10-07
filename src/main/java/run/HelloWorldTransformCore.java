package run;

import lsieun.asm.function.InsnInvokeConsumer;
import lsieun.asm.function.InsnInvokeMatch;
import lsieun.asm.function.MethodMatch;
import lsieun.asm.search.SearchItem;
import lsieun.asm.utils.ClassFileFindUtils;
import lsieun.asm.utils.ClassFileModifyUtils;
import lsieun.asm.utils.CodeSegmentUtils;
import lsieun.utils.io.resource.ResourceUtils;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class HelloWorldTransformCore {
    public static void main(String[] args) throws IOException {
        String relativePath = "sample/HelloWorld.class";
        Path path = ResourceUtils.readFilePath(relativePath);
        byte[] bytes1 = Files.readAllBytes(path);
//        byte[] bytes2 = ClassFileModifyUtils.patchInsnInvoke(
//                bytes1,
//                MethodMatch.AllMethods.INSTANCE,
//                InsnInvokeMatch.ByReturn.METHOD,
//                new InsnInvokeConsumer() {
//                    @Override
//                    public void accept(MethodVisitor mv, int opcode, String owner, String name, String descriptor, boolean isInterface) {
//                        mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
//                        Type t = Type.getMethodType(descriptor);
//                        Type returnType = t.getReturnType();
//                        CodeSegmentUtils.printReturnValue(mv, returnType);
//                        CodeSegmentUtils.printMessage(mv, "Haha");
//                    }
//                });
//        Files.write(path, bytes2);

        List<SearchItem> itemList = ClassFileFindUtils.findInsnInvoke(bytes1,
                MethodMatch.AllMethods.INSTANCE,
                InsnInvokeMatch.Common.SYSTEM_EXIT);
        itemList.forEach(System.out::println);
    }
}