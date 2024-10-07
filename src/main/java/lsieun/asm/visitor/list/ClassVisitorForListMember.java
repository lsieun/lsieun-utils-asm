package lsieun.asm.visitor.list;

import lsieun.asm.cst.MyConst;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.lang.reflect.Modifier;
import java.util.Arrays;

public class ClassVisitorForListMember extends ClassVisitor {
    public ClassVisitorForListMember() {
        super(MyConst.API_VERSION, null);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        String msg = String.format("Java %s: %s %s extends %s implements %s",
                (version - 44), Modifier.toString(access), name, superName, Arrays.toString(interfaces));
        System.out.println(msg);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        String msg = String.format("    %s %s:%s", Modifier.toString(access), name, descriptor);
        System.out.println(msg);
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        String msg = String.format("    %s %s:%s", Modifier.toString(access), name, descriptor);
        System.out.println(msg);
        return null;
    }
}
