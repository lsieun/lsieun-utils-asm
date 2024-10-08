package lsieun.asm.visitor.common;

import lsieun.asm.cst.MyConst;
import lsieun.asm.description.ByteCodeElementType;
import lsieun.asm.function.match.InsnInvokeMatch;
import lsieun.asm.function.match.MatchFormat;
import lsieun.asm.function.match.MatchState;
import lsieun.utils.log.Logger;
import lsieun.utils.log.LoggerFactory;
import org.objectweb.asm.MethodVisitor;

public abstract class MethodVisitorForInsnInvokeMatch extends MethodVisitor {
    private static final Logger logger = LoggerFactory.getLogger(MethodVisitorForInsnInvokeMatch.class);

    protected final String currentType;
    protected final String currentMethodName;
    protected final String currentMethodDesc;
    private final InsnInvokeMatch insnInvokeMatch;

    protected MethodVisitorForInsnInvokeMatch(MethodVisitor methodVisitor,
                                              String currentType, String currentMethodName, String currentMethodDesc,
                                              InsnInvokeMatch insnInvokeMatch) {
        super(MyConst.API_VERSION, methodVisitor);
        this.currentType = currentType;
        this.currentMethodName = currentMethodName;
        this.currentMethodDesc = currentMethodDesc;
        this.insnInvokeMatch = insnInvokeMatch;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        logger.trace(() -> MatchFormat.format(MatchState.MATCHING, ByteCodeElementType.INSN, owner, name, descriptor));
        boolean flag = insnInvokeMatch.test(opcode, owner, name, descriptor);
        if (flag) {
            logger.debug(() -> MatchFormat.format(MatchState.MATCHED, ByteCodeElementType.INSN, owner, name, descriptor));
            onMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
        else {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }

    protected abstract void onMethodInsn(int opcode, String owner,
                                         String name, String descriptor, boolean isInterface);
}
