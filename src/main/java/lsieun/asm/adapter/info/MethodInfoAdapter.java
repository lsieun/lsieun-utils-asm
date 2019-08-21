package lsieun.asm.adapter.info;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

import lsieun.asm.adapter.RegexAdapter;
import lsieun.asm.cst.Constant;
import lsieun.asm.utils.CodeUtils;
import lsieun.asm.utils.NameUtils;
import lsieun.utils.RegexUtils;

public class MethodInfoAdapter extends RegexAdapter {
    private static int display_order = 0;

    // 开始前，传入参数
    private boolean showMethodName;
    private boolean showMethodArgs;
    private boolean showMethodReturnValue;
    private boolean showStackTrace;

    public MethodInfoAdapter(ClassVisitor classVisitor, String[] regex_array, boolean showMethodName, boolean showMethodArgs, boolean showMethodReturnValue, boolean showStackTrace) {
        super(classVisitor, regex_array);
        this.showMethodName = showMethodName;
        this.showMethodArgs = showMethodArgs;
        this.showMethodReturnValue = showMethodReturnValue;
        this.showStackTrace = showStackTrace;
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
            System.out.println(String.format("%s(%s-MethodInfoAdapter)%s: %s", System.lineSeparator(), display_order,"ClassName", NameUtils.getFQCN(internalName)));
            hasPrintClassName = true;
        }
        System.out.println(String.format("method: %s:%s", name, descriptor));
        mv = new MethodInfoVisitor(mv, access, name, descriptor);

        return mv;
    }

    class MethodInfoVisitor extends AdviceAdapter {

        public MethodInfoVisitor(MethodVisitor methodVisitor, int access, String name, String descriptor) {
            super(Constant.API_VERSION, methodVisitor, access, name, descriptor);
        }

        @Override
        protected void onMethodEnter() {
            if (mv != null) {
                CodeUtils.printMessage(mv, System.lineSeparator());
                if (showMethodName) {
                    CodeUtils.printMethodName(mv, internalName, getName(), methodDesc);
                }

                if (showMethodArgs) {
                    CodeUtils.printMethodArguments(mv, methodAccess, methodDesc);
                }

                if (showStackTrace) {
                    CodeUtils.printStackTrace(mv);
                }

            }
        }

        @Override
        protected void onMethodExit(int opcode) {
            if (mv != null) {
                if (showMethodReturnValue) {
                    CodeUtils.printMethodReturnValue(mv, opcode, methodDesc);
                }
                CodeUtils.printMessage(mv, System.lineSeparator());
            }
        }
    }
}
