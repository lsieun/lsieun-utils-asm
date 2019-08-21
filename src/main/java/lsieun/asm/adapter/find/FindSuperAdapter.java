package lsieun.asm.adapter.find;

import org.objectweb.asm.ClassVisitor;

import lsieun.asm.adapter.RegexAdapter;
import lsieun.asm.utils.NameUtils;
import lsieun.utils.RegexUtils;

public class FindSuperAdapter extends RegexAdapter {
    private static int display_order = 0;

    public FindSuperAdapter(ClassVisitor classVisitor, String[] regex_array) {
        super(classVisitor, regex_array);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        // 先处理自己的逻辑
        if (RegexUtils.matches(superName, regex_array)) {
            display_order++;
            System.out.println(String.format("%s(%s) %s: %s ===> %s", System.lineSeparator(), display_order, "ClassName", NameUtils.getFQCN(name), NameUtils.getFQCN(superName)));
        }

        // 再处理别人的逻辑
        super.visit(version, access, name, signature, superName, interfaces);
    }
}
