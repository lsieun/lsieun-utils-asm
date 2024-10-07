package lsieun.asm.visitor.find;

import lsieun.asm.cst.MyConst;
import lsieun.asm.search.SearchItem;
import lsieun.asm.visitor.ClassRegexVisitor;
import org.objectweb.asm.MethodVisitor;

import static lsieun.asm.utils.ASMStringUtils.getClassMemberInfo;

public class FindInvokeInstructionRegexVisitor extends ClassRegexVisitor {
    public FindInvokeInstructionRegexVisitor(String[] includes, String[] excludes, String[] invokeRegex) {
        super(null, includes, excludes);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        String name_desc = getClassMemberInfo(name, descriptor);
        boolean flag = isAppropriate(name_desc);
        if (flag) {
            return new FindInvokeInstructionRegexAdapter();
        }
        return null;
    }

    private class FindInvokeInstructionRegexAdapter extends MethodVisitor {

        public FindInvokeInstructionRegexAdapter() {
            super(MyConst.API_VERSION, null);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            SearchItem item = SearchItem.ofMethod(owner, name, descriptor);
            addResult(item);
        }
    }
}
