package lsieun.asm.adapter.find;

import org.objectweb.asm.ClassVisitor;

import lsieun.asm.adapter.RegexAdapter;
import lsieun.asm.utils.NameUtils;
import lsieun.utils.RegexUtils;

public class FindInterfaceAdapter extends RegexAdapter {
    private static int display_order = 0;

    public FindInterfaceAdapter(ClassVisitor classVisitor, String[] regex_array) {
        super(classVisitor, regex_array);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        // 先处理自己的逻辑
        if (interfaces != null && interfaces.length > 0) {
            for (String item : interfaces) {
                if (RegexUtils.matches(item, regex_array)) {
                    display_order++;
                    System.out.println(String.format("%s(%s) %s: %s ---> %s", System.lineSeparator(), display_order, "ClassName", NameUtils.getFQCN(name), NameUtils.getFQCN(item)));
                    break;
                }
            }
        }

        // 再处理别人的逻辑
        super.visit(version, access, name, signature, superName, interfaces);
    }
}
