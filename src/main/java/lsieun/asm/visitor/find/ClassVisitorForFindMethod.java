package lsieun.asm.visitor.find;

import lsieun.asm.cst.MyConst;
import lsieun.asm.function.MethodMatch;
import lsieun.asm.function.MultipleResult;
import lsieun.asm.search.SearchItem;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.List;

public class ClassVisitorForFindMethod extends ClassVisitor implements MultipleResult {
    protected int version;
    protected String currentType;
    private final MethodMatch methodMatch;
    private final List<SearchItem> resultList = new ArrayList<>();

    public ClassVisitorForFindMethod(MethodMatch methodMatch) {
        super(MyConst.API_VERSION);
        this.methodMatch = methodMatch;
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
            SearchItem item = SearchItem.ofMethod(currentType, name, descriptor);
            if(!resultList.contains(item)) {
                resultList.add(item);
            }
        }
        return null;
    }

    @Override
    public List<SearchItem> getResultList() {
        return resultList;
    }
}
