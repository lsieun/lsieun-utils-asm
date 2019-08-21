package lsieun.asm.adapter.find;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

import lsieun.asm.adapter.RegexAdapter;
import lsieun.asm.utils.NameUtils;
import lsieun.utils.RegexUtils;

public class FindFieldAdapter extends RegexAdapter {
    private static int display_order = 0;

    public FindFieldAdapter(ClassVisitor classVisitor, String[] regex_array) {
        super(classVisitor, regex_array);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        // 先处理自己的逻辑
        String name_desc = String.format("%s:%s", name, descriptor);
        if (RegexUtils.matches(name_desc, regex_array)) {
            if (!hasPrintClassName) {
                display_order++;
                System.out.println(String.format("%s(%s) %s: %s", System.lineSeparator(), display_order, "ClassName", NameUtils.getFQCN(internalName)));
                hasPrintClassName = true;
            }
            System.out.println(String.format("field: %s:%s", name, descriptor));
        }

        // 再处理别人的逻辑
        return super.visitField(access, name, descriptor, signature, value);
    }
}
