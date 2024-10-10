package lsieun.asm.function.match;

import org.objectweb.asm.Type;

@FunctionalInterface
public interface MethodMatch {
    boolean test(int version, String owner,
                 int methodAccess, String methodName, String methodDesc,
                 String signature, String[] exceptions);

    enum AllMethods implements MethodMatch {
        INSTANCE;

        @Override
        public boolean test(int version, String owner,
                            int methodAccess, String methodName, String methodDesc,
                            String signature, String[] exceptions) {
            return true;
        }
    }

    enum Bool implements MethodMatch {
        TRUE {
            @Override
            public boolean test(int version, String owner,
                                int methodAccess, String methodName, String methodDesc,
                                String signature, String[] exceptions) {
                return true;
            }
        },
        FALSE {
            @Override
            public boolean test(int version, String owner,
                                int methodAccess, String methodName, String methodDesc,
                                String signature, String[] exceptions) {
                return false;
            }
        };
    }

//    class And implements MethodMatch {
//        private final MethodMatch[] matches;
//
//        private And(final MethodMatch... matches) {
//            this.matches = matches;
//        }
//
//        @Override
//        public boolean test(int version, String owner,
//                            int methodAccess, String methodName, String methodDesc,
//                            String signature, String[] exceptions) {
//            for (MethodMatch match : matches) {
//                if (!match.test(version, owner, methodAccess, methodName, methodDesc, signature, exceptions)) {
//                    return false;
//                }
//            }
//            return true;
//        }
//
//        public static And of(final MethodMatch... matches) {
//            return new And(matches);
//        }
//    }

//    class Or implements MethodMatch {
//        private final MethodMatch[] matches;
//
//        private Or(final MethodMatch... matches) {
//            this.matches = matches;
//        }
//
//        @Override
//        public boolean test(int version, String owner,
//                            int methodAccess, String methodName, String methodDesc,
//                            String signature, String[] exceptions) {
//            for (MethodMatch match : matches) {
//                if (match.test(version, owner, methodAccess, methodName, methodDesc, signature, exceptions)) {
//                    return true;
//                }
//            }
//            return false;
//        }
//
//        public static Or of(final MethodMatch... matches) {
//            return new Or(matches);
//        }
//    }

    static MethodMatch byMethodName(String name) {
        return byMethodName(TextMatch.equals(name));
    }

    static MethodMatch byMethodName(TextMatch textMatch) {
        return (version, owner, methodAccess, methodName, methodDesc, signature, exceptions) ->
                textMatch.test(methodName);
    }

    static MethodMatch byMethodNameAndDesc(String name, String desc) {
        return (version, owner, methodAccess, methodName, methodDesc, signature, exceptions) ->
                methodName.equals(name) && methodDesc.equals(desc);
    }

    static MethodMatch byMethodNameAndDesc(TextMatch textMatch) {
        return (version, owner, methodAccess, methodName, methodDesc, signature, exceptions) ->
        {
            String nameAndDesc = String.format("%s:%s", methodName, methodDesc);
            return textMatch.test(nameAndDesc);
        };
    }

    static MethodMatch byOwnerMethodNameAndDesc(String internalClassName, String name, String desc) {
        return (version, owner, methodAccess, methodName, methodDesc, signature, exceptions) ->
                owner.equals(internalClassName) && methodName.equals(name) && methodDesc.equals(desc);
    }

    static MethodMatch byReturnType(TypeMatch typeMatch) {
        return (version, owner, methodAccess, methodName, methodDesc, signature, exceptions) ->
        {
            Type returnType = Type.getReturnType(methodDesc);
            return typeMatch.test(returnType);
        };
    }
}
