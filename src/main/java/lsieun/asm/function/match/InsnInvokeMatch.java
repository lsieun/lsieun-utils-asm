package lsieun.asm.function.match;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;

/**
 * @see InsnInvokeMatchBuddy
 */
public interface InsnInvokeMatch {
    boolean test(int opcode, String owner, String name, String descriptor);

    enum Bool implements InsnInvokeMatch {
        TRUE {
            @Override
            public boolean test(int opcode, String owner, String name, String descriptor) {
                return true;
            }
        },
        FALSE {
            @Override
            public boolean test(int opcode, String owner, String name, String descriptor) {
                return false;
            }
        };
    }

    class And implements InsnInvokeMatch {
        private final InsnInvokeMatch[] matches;

        private And(InsnInvokeMatch... matches) {
            this.matches = matches;
        }

        @Override
        public boolean test(int opcode, String owner, String name, String descriptor) {
            for (InsnInvokeMatch match : matches) {
                if (!match.test(opcode, owner, name, descriptor)) {
                    return false;
                }
            }
            return true;
        }

        public static And of(InsnInvokeMatch... matches) {
            return new And(matches);
        }
    }

    class Or implements InsnInvokeMatch {
        private final InsnInvokeMatch[] matches;

        private Or(InsnInvokeMatch... matches) {
            this.matches = matches;
        }

        @Override
        public boolean test(int opcode, String owner, String name, String descriptor) {
            for (InsnInvokeMatch match : matches) {
                if (match.test(opcode, owner, name, descriptor)) {
                    return true;
                }
            }
            return false;
        }

        public static Or of(InsnInvokeMatch... matches) {
            return new Or(matches);
        }
    }

    enum ByMethodInsn implements InsnInvokeMatch {
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
