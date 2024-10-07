package lsieun.asm.visitor.find;

import lsieun.asm.search.SearchItem;
import lsieun.asm.visitor.ClassRegexVisitor;

public class FindInterfaceRegexVisitor extends ClassRegexVisitor {
    public FindInterfaceRegexVisitor(String[] includes, String[] excludes) {
        super(null, includes, excludes);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (interfaces == null || interfaces.length == 0) {
            return;
        }
        for (String itf : interfaces) {
            boolean flag = isAppropriate(itf);
            if (flag) {
                SearchItem item = SearchItem.ofType(name);
                addResult(item);
            }
        }
    }
}
