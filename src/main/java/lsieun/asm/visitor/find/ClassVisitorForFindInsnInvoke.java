package lsieun.asm.visitor.find;

import lsieun.asm.cst.MyConst;
import lsieun.asm.function.InsnInvokeMatch;
import lsieun.asm.function.MethodMatch;
import lsieun.asm.function.MultipleResult;
import lsieun.asm.search.SearchItem;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.List;

public class ClassVisitorForFindInsnInvoke extends ClassVisitor implements MultipleResult {
    protected int version;
    protected String currentType;
    private final MethodMatch methodMatch;
    private final InsnInvokeMatch insnInvokeMatch;
    private final List<SearchItem> resultList = new ArrayList<>();

    public ClassVisitorForFindInsnInvoke(MethodMatch methodMatch, InsnInvokeMatch insnInvokeMatch) {
        super(MyConst.API_VERSION);
        this.methodMatch = methodMatch;
        this.insnInvokeMatch = insnInvokeMatch;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.version = version;
        this.currentType = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        boolean flag = methodMatch.test(version, currentType, access, name, descriptor, signature, exceptions);
        if (flag) {
            return new MethodVisitorForFindInsnInvoke(name, descriptor);
        }
        else {
            return null;
        }
    }

    @Override
    public List<SearchItem> getResultList() {
        return resultList;
    }

    private class MethodVisitorForFindInsnInvoke extends MethodVisitor {
        protected final String currentMethodName;
        protected final String currentMethodDesc;

        public MethodVisitorForFindInsnInvoke(String currentMethodName, String currentMethodDesc) {
            super(MyConst.API_VERSION);
            this.currentMethodName = currentMethodName;
            this.currentMethodDesc = currentMethodDesc;
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            boolean flag = insnInvokeMatch.test(opcode, owner, name, descriptor);
            if (flag) {
                SearchItem item = SearchItem.ofMethod(currentType, currentMethodName, currentMethodDesc);
                if(!resultList.contains(item)) {
                    resultList.add(item);
                }
            }
            else {
                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            }
        }
    }
}
