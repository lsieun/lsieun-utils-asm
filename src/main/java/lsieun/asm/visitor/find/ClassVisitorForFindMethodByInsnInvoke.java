package lsieun.asm.visitor.find;

import lsieun.asm.cst.MyConst;
import lsieun.asm.description.ByteCodeElementType;
import lsieun.asm.function.match.InsnInvokeMatch;
import lsieun.asm.function.match.MatchFormat;
import lsieun.asm.function.match.MatchState;
import lsieun.asm.function.match.MethodMatch;
import lsieun.asm.search.SearchItem;
import lsieun.utils.log.Logger;
import lsieun.utils.log.LoggerFactory;
import org.objectweb.asm.MethodVisitor;

public class ClassVisitorForFindMethodByInsnInvoke extends ClassVisitorForFind {
    public static final Logger logger = LoggerFactory.getLogger(ClassVisitorForFindMethodByInsnInvoke.class);

    private final MethodMatch methodMatch;
    private final InsnInvokeMatch insnInvokeMatch;

    public ClassVisitorForFindMethodByInsnInvoke(MethodMatch methodMatch, InsnInvokeMatch insnInvokeMatch) {
        this.methodMatch = methodMatch;
        this.insnInvokeMatch = insnInvokeMatch;
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        logger.trace(() -> MatchFormat.format(MatchState.MATCHING, ByteCodeElementType.METHOD, currentOwner, name, descriptor));
        boolean flag = methodMatch.test(version, currentOwner, access, name, descriptor, signature, exceptions);
        if (flag) {
            logger.debug(() -> MatchFormat.format(MatchState.MATCHED, ByteCodeElementType.METHOD, currentOwner, name, descriptor));
            return new MethodVisitorForFindInsnInvoke(name, descriptor);
        }
        else {
            return null;
        }
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
            logger.trace(() -> MatchFormat.format(MatchState.MATCHING, ByteCodeElementType.INSN, owner, name, descriptor));
            boolean flag = insnInvokeMatch.test(opcode, owner, name, descriptor);

            if (flag) {
                logger.debug(() -> MatchFormat.format(MatchState.MATCHED, ByteCodeElementType.INSN, owner, name, descriptor));
                SearchItem item = SearchItem.ofMethod(currentOwner, currentMethodName, currentMethodDesc);
                if (!resultList.contains(item)) {
                    resultList.add(item);
                }
            }
        }
    }
}
