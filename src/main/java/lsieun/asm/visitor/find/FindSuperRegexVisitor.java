package lsieun.asm.visitor.find;

import lsieun.asm.search.SearchItem;
import lsieun.asm.visitor.ClassRegexVisitor;

public class FindSuperRegexVisitor extends ClassRegexVisitor {

    public FindSuperRegexVisitor(String[] includes) {
        super(null, includes, null);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        boolean flag = isAppropriate(superName);
        if (flag) {
            SearchItem item = SearchItem.ofType(name);
            addResult(item);
        }
    }
}
