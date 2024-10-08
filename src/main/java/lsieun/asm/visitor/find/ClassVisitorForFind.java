package lsieun.asm.visitor.find;

import lsieun.asm.cst.MyConst;
import lsieun.asm.function.match.MatchResult;
import lsieun.asm.search.SearchItem;
import org.objectweb.asm.ClassVisitor;

import java.util.ArrayList;
import java.util.List;

public class ClassVisitorForFind extends ClassVisitor implements MatchResult {
    protected int version;
    protected String currentOwner;
    protected final List<SearchItem> resultList = new ArrayList<>();

    protected ClassVisitorForFind() {
        super(MyConst.API_VERSION);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.version = version;
        this.currentOwner = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public List<SearchItem> getResultList() {
        return resultList;
    }
}
