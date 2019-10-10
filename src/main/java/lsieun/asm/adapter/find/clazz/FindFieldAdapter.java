package lsieun.asm.adapter.find.clazz;

import lsieun.asm.adapter.RegexAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

public class FindFieldAdapter extends RegexAdapter {

    public FindFieldAdapter(ClassVisitor classVisitor, String[] includes, String[] excludes) {
        super(classVisitor, includes, excludes);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        // 先处理自己的逻辑
        isTargetMember(name, descriptor);

        // 再处理别人的逻辑
        return super.visitField(access, name, descriptor, signature, value);
    }
}
