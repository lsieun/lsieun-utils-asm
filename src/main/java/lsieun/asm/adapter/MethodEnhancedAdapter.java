package lsieun.asm.adapter;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import lsieun.asm.utils.NameUtils;
import lsieun.utils.RegexUtils;

public abstract class MethodEnhancedAdapter extends RegexAdapter {
    public int display_order = 0;

    public MethodEnhancedAdapter(ClassVisitor classVisitor, String[] regex_array) {
        super(classVisitor, regex_array);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

        // （1） 接口，不处理
        if (isInterface) return mv;

        //（2）抽象方法，不处理
        boolean isAbstractMethod = (access & ACC_ABSTRACT) != 0;
        if (isAbstractMethod) return mv;

        // （3）方法名和描述符不符合要求，不处理。
        String name_desc = String.format("%s:%s", name, descriptor);
        if (!RegexUtils.matches(name_desc, regex_array)) return mv;

        // （4）条件符合了，可以进行处理了
        gotcha = true;
        if (!hasPrintClassName) {
            display_order++;
            System.out.println(String.format("%s(%s)%s: %s", System.lineSeparator(), display_order,"ClassName", NameUtils.getFQCN(internalName)));
            hasPrintClassName = true;
        }
        System.out.println(String.format("method: %s:%s", name, descriptor));
        mv = enhanceMethodVisitor(mv, access, name, descriptor, signature, exceptions);

        return mv;
    }

    protected abstract MethodVisitor enhanceMethodVisitor(MethodVisitor mv, int access, String name, String descriptor, String signature, String[] exceptions);

}
