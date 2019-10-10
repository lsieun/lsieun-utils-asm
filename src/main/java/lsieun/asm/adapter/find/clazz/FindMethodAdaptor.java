package lsieun.asm.adapter.find.clazz;

import lsieun.asm.adapter.RegexAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class FindMethodAdaptor extends RegexAdapter {
    public FindMethodAdaptor(ClassVisitor classVisitor, String[] includes, String[] excludes) {
        super(classVisitor, includes, excludes);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        // 先处理自己的逻辑
        isTargetMember(name, descriptor);

        // 再处理别人的逻辑
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
