package lsieun.asm.visitor.find;

import lsieun.asm.description.ByteCodeElementType;
import lsieun.asm.function.match.MatchFormat;
import lsieun.asm.function.match.MatchState;
import lsieun.asm.function.match.MethodMatch;
import lsieun.asm.search.SearchItem;
import lsieun.utils.log.Logger;
import lsieun.utils.log.LoggerFactory;
import org.objectweb.asm.MethodVisitor;

public class ClassVisitorForFindMethod extends ClassVisitorForFind {
    private static final Logger logger = LoggerFactory.getLogger(ClassVisitorForFindMethod.class);

    private final MethodMatch methodMatch;

    public ClassVisitorForFindMethod(MethodMatch methodMatch) {
        this.methodMatch = methodMatch;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        logger.trace(() -> MatchFormat.format(MatchState.MATCHING, ByteCodeElementType.METHOD, currentOwner, name, descriptor));
        boolean flag = methodMatch.test(version, currentOwner, access, name, descriptor, signature, exceptions);

        if (flag) {
            logger.debug(() -> MatchFormat.format(MatchState.MATCHED, ByteCodeElementType.METHOD, currentOwner, name, descriptor));
            SearchItem item = SearchItem.ofMethod(currentOwner, name, descriptor);
            if (!resultList.contains(item)) {
                resultList.add(item);
            }
        }
        return null;
    }
}
