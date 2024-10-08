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

public class ClassVisitorForFindInsnByInsnInvoke extends ClassVisitorForFind {
    public static final Logger logger = LoggerFactory.getLogger(ClassVisitorForFindInsnByInsnInvoke.class);

    private final MethodMatch methodMatch;
    private final InsnInvokeMatch insnInvokeMatch;

    public ClassVisitorForFindInsnByInsnInvoke(MethodMatch methodMatch, InsnInvokeMatch insnInvokeMatch) {
        this.methodMatch = methodMatch;
        this.insnInvokeMatch = insnInvokeMatch;
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        logger.trace(() -> MatchFormat.format(MatchState.MATCHING, ByteCodeElementType.METHOD, currentOwner, name, descriptor));
        boolean flag = methodMatch.test(version, currentOwner, access, name, descriptor, signature, exceptions);
        if (flag) {
            logger.debug(() -> MatchFormat.format(MatchState.MATCHED, ByteCodeElementType.METHOD, currentOwner, name, descriptor));
            return new MethodVisitorForFindInsnInvoke();
        }
        else {
            return null;
        }
    }


    private class MethodVisitorForFindInsnInvoke extends MethodVisitor {

        public MethodVisitorForFindInsnInvoke() {
            super(MyConst.API_VERSION);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            logger.trace(() -> MatchFormat.format(MatchState.MATCHING, ByteCodeElementType.INSN, owner, name, descriptor));
            boolean flag = insnInvokeMatch.test(opcode, owner, name, descriptor);

            if (flag) {
                logger.debug(() -> MatchFormat.format(MatchState.MATCHED, ByteCodeElementType.INSN, owner, name, descriptor));
                SearchItem item = SearchItem.ofMethod(owner, name, descriptor);
                resultList.add(item);
            }
        }
    }
}
