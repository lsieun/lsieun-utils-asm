package lsieun.asm.visitor;

public class FindSuperRegexVisitor extends ClassRegexVisitor {

    public FindSuperRegexVisitor(String[] includes) {
        super(null, includes, null);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        boolean flag = isAppropriate(superName);
        if (flag) {
            if (!resultList.contains(name)) {
                resultList.add(name);
            }
        }
    }
}
