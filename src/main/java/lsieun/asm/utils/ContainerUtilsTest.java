package lsieun.asm.utils;

import java.security.ProtectionDomain;

public class ContainerUtilsTest {

    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) {

        ContainerUtils.start(new ContainerUtils(className, classfileBuffer));

        byte[] bytes = new byte[10];
        ContainerUtils.stop(bytes, className);

        return null;
    }


    public static void main(String[] args) {
        ASMPrint.generate("lsieun.asm.utils.ContainerUtilsTest", true);
    }
}
