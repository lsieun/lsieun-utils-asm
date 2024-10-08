package lsieun.asm.function.match;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;

/**
 * @see InsnInvokeMatchBuddy
 */
public interface InsnInvokeMatch {
    boolean test(int opcode, String owner, String name, String descriptor);

    enum Binary implements InsnInvokeMatch {
        TRUE {
            @Override
            public boolean test(int opcode, String owner, String name, String descriptor) {
                return true;
            }
        },
        FALSE{
            @Override
            public boolean test(int opcode, String owner, String name, String descriptor) {
                return false;
            }
        };
    }

    enum Common implements InsnInvokeMatch {
        SYSTEM_EXIT {
            @Override
            public boolean test(int opcode, String owner, String name, String descriptor) {
                return opcode == Opcodes.INVOKESTATIC &&
                        "java/lang/System".equals(owner) &&
                        "exit".equals(name) &&
                        "(I)V".equals(descriptor);
            }
        },
        METHOD_INVOKE {
            @Override
            public boolean test(int opcode, String owner, String name, String descriptor) {
                return opcode == Opcodes.INVOKEVIRTUAL &&
                        "java/lang/reflect/Method".equals(owner) &&
                        "invoke".equals(name) &&
                        "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;".equals(descriptor);
            }
        };
    }

    enum ByReturn implements InsnInvokeMatch {
        METHOD {
            @Override
            public boolean test(int opcode, String owner, String name, String descriptor) {
                Type t = Type.getMethodType(descriptor);
                Type returnType = t.getReturnType();
                return Type.getType(Method.class).equals(returnType);
            }
        },
        ;
    }
}
