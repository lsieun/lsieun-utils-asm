package lsieun.asm.adapter;

import static org.objectweb.asm.Opcodes.ACC_INTERFACE;

import org.objectweb.asm.ClassVisitor;

import lsieun.asm.cst.Constant;

public class RegexAdapter extends ClassVisitor {
    // 开始前，传入的参数
    public final String[] regex_array;

    // 过程中，记录的参数
    public String internalName;
    public boolean isInterface;
    public boolean hasPrintClassName;

    // 结束后，输出的参数
    public boolean gotcha = false;

    public RegexAdapter(ClassVisitor classVisitor, String[] regex_array) {
        super(Constant.API_VERSION, classVisitor);
        this.regex_array = regex_array;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        // 先处理自己的逻辑
        internalName = name;
        hasPrintClassName = false;
        isInterface = (access & ACC_INTERFACE) != 0;

        // 再处理别人的逻辑
        super.visit(version, access, name, signature, superName, interfaces);
    }

}
