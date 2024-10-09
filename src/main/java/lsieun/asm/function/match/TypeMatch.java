package lsieun.asm.function.match;

import org.objectweb.asm.Type;

@FunctionalInterface
public interface TypeMatch {
    boolean test(Type type);

    enum Bool implements TypeMatch {
        TRUE {
            @Override
            public boolean test(Type type) {
                return true;
            }
        },
        FALSE {
            @Override
            public boolean test(Type type) {
                return false;
            }
        };
    }

    class And implements TypeMatch {
        private final TypeMatch[] matches;

        private And(TypeMatch... matches) {
            this.matches = matches;
        }

        @Override
        public boolean test(Type type) {
            for (TypeMatch match : matches) {
                if (!match.test(type)) {
                    return false;
                }
            }
            return true;
        }

        public static And of(TypeMatch... matches) {
            return new And(matches);
        }
    }

    class Or implements TypeMatch {
        private final TypeMatch[] matches;

        private Or(TypeMatch... matches) {
            this.matches = matches;
        }

        @Override
        public boolean test(Type type) {
            for (TypeMatch match : matches) {
                if (match.test(type)) {
                    return true;
                }
            }
            return false;
        }

        public static Or of(TypeMatch... matches) {
            return new Or(matches);
        }
    }
}
