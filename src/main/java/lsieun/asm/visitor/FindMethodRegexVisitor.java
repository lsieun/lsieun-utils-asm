package lsieun.asm.visitor;

import lsieun.asm.cst.Constant;
import org.objectweb.asm.MethodVisitor;

public class FindMethodRegexVisitor extends ClassRegexVisitor {
    private String owner;

    public FindMethodRegexVisitor(String[] includes, String[] excludes) {
        super(null, includes, excludes);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.owner = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        String name_and_desc = String.format("%s%s%s", name, Constant.COLON, descriptor);
        boolean flag = isAppropriate(name_and_desc);
        if (flag) {
            String item = String.format("%s.class %s:%s", owner, name, descriptor);
            if (!resultList.contains(item)) {
                resultList.add(item);
            }
        }

        return null;
    }
}
