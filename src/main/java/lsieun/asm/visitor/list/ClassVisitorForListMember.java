package lsieun.asm.visitor.list;

import lsieun.asm.cst.MyConst;
import lsieun.asm.function.match.MemberMatch;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.lang.reflect.Modifier;
import java.util.Arrays;

public class ClassVisitorForListMember extends ClassVisitor {
    private String currentOwner;
    private final MemberMatch memberMatch;

    public ClassVisitorForListMember() {
        this(MemberMatch.Bool.TRUE);
    }

    public ClassVisitorForListMember(MemberMatch memberMatch) {
        super(MyConst.API_VERSION, null);
        this.memberMatch = memberMatch;
    }


    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.currentOwner = name;
        String msg = String.format("Java %s: %s %s extends %s implements %s",
                (version - 44), Modifier.toString(access), name, superName, Arrays.toString(interfaces));
        System.out.println(msg);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if (memberMatch.test(currentOwner, access, name, descriptor)) {
            String msg = String.format("    %s %s:%s", Modifier.toString(access), name, descriptor);
            System.out.println(msg);
        }

        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (memberMatch.test(currentOwner, access, name, descriptor)) {
            String msg = String.format("    %s %s:%s", Modifier.toString(access), name, descriptor);
            System.out.println(msg);
        }
        return null;
    }
}
