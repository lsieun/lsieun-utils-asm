package lsieun.asm.function.match;

@FunctionalInterface
public interface MethodDescMatch {
    boolean test(String methodDesc);

    enum Bool implements MethodDescMatch {
        TRUE {
            @Override
            public boolean test(String methodDesc) {
                return true;
            }
        },
        FALSE {
            @Override
            public boolean test(String methodDesc) {
                return false;
            }
        };
    }

    class And implements MethodDescMatch {
        private final MethodDescMatch[] matches;

        private And(MethodDescMatch... matches) {
            this.matches = matches;
        }

        @Override
        public boolean test(String methodDesc) {
            for (MethodDescMatch match : matches) {
                if (!match.test(methodDesc)) {
                    return false;
                }
            }
            return true;
        }

        public static And of(MethodDescMatch... matches) {
            return new And(matches);
        }
    }

    class Or implements MethodDescMatch {
        private final MethodDescMatch[] matches;

        private Or(MethodDescMatch... matches) {
            this.matches = matches;
        }

        @Override
        public boolean test(String methodDesc) {
            for (MethodDescMatch match : matches) {
                if (match.test(methodDesc)) {
                    return true;
                }
            }
            return false;
        }

        public static Or of(MethodDescMatch... matches) {
            return new Or(matches);
        }
    }
}
