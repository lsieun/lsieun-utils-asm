package lsieun.asm.visitor.find;

import lsieun.asm.search.SearchItem;
import lsieun.asm.utils.ASMStringUtils;
import lsieun.asm.visitor.ClassRegexVisitor;
import org.objectweb.asm.FieldVisitor;

public class FindFieldRegexVisitor extends ClassRegexVisitor {
    private String owner;

    public FindFieldRegexVisitor(String[] includes, String[] excludes) {
        super(null, includes, excludes);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.owner = name;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        String name_and_desc = ASMStringUtils.getClassMemberInfo(name, descriptor);
        boolean flag = isAppropriate(name_and_desc);
        if (flag) {
            SearchItem item = SearchItem.ofField(owner, name, descriptor);
            addResult(item);
        }

        return null;
    }
}
