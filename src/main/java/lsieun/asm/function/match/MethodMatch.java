package lsieun.asm.function.match;

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

    enum Binary implements MethodMatch {
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

    class And implements MethodMatch {
        private final MethodMatch[] matches;

        private And(final MethodMatch... matches) {
            this.matches = matches;
        }

        @Override
        public boolean test(int version, String owner,
                            int methodAccess, String methodName, String methodDesc,
                            String signature, String[] exceptions) {
            for (MethodMatch match : matches) {
                if (!match.test(version, owner, methodAccess, methodName, methodDesc, signature, exceptions)) {
                    return false;
                }
            }
            return true;
        }

        public static And of(final MethodMatch... matches) {
            return new And(matches);
        }
    }

    class Or implements MethodMatch {
        private final MethodMatch[] matches;

        private Or(final MethodMatch... matches) {
            this.matches = matches;
        }

        @Override
        public boolean test(int version, String owner,
                            int methodAccess, String methodName, String methodDesc,
                            String signature, String[] exceptions) {
            for (MethodMatch match : matches) {
                if (match.test(version, owner, methodAccess, methodName, methodDesc, signature, exceptions)) {
                    return true;
                }
            }
            return false;
        }

        public static Or of(final MethodMatch... matches) {
            return new Or(matches);
        }
    }
}
