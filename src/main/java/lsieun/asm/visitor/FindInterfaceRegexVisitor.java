package lsieun.asm.visitor;

public class FindInterfaceRegexVisitor extends ClassRegexVisitor {
    public FindInterfaceRegexVisitor(String[] includes, String[] excludes) {
        super(null, includes, excludes);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (interfaces == null || interfaces.length == 0) return;
        for (String item : interfaces) {
            boolean flag = isAppropriate(item);
            if (flag) {
                if (!resultList.contains(name)) {
                    resultList.add(name);
                }
            }
        }
    }
}
