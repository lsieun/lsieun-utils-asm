package lsieun.asm.adapter.find.clazz;

import lsieun.asm.adapter.RegexAdapter;
import org.objectweb.asm.ClassVisitor;

public class FindSuperAdapter extends RegexAdapter {

    public FindSuperAdapter(ClassVisitor classVisitor, String[] includes) {
        super(classVisitor, includes, null);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        // 先处理父类的逻辑
        super.visit(version, access, name, signature, superName, interfaces);

        // 再处理本身的逻辑
        isTargetClassName(superName);
    }
}
