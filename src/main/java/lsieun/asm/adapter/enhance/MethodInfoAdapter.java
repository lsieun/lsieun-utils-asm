package lsieun.asm.adapter.enhance;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

import lsieun.asm.adapter.MethodEnhancedAdapter;
import lsieun.asm.cst.Constant;
import lsieun.asm.utils.CodeUtils;

public class MethodInfoAdapter extends MethodEnhancedAdapter {

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
    protected MethodVisitor enhanceMethodVisitor(MethodVisitor mv, int access, String name, String descriptor, String signature, String[] exceptions) {
        return new MethodInfoVisitor(mv, access, name, descriptor);
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
