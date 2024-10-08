package lsieun.asm.function.match;

import org.objectweb.asm.Type;

@FunctionalInterface
public interface TypeMatch {
    boolean test(Type type);

    enum Binary implements TypeMatch {
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
        private final TypeMatch left;
        private final TypeMatch right;

        private And(final TypeMatch left, final TypeMatch right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public boolean test(Type type) {
            return left.test(type) && right.test(type);
        }

        public static And of(final TypeMatch left, final TypeMatch right) {
            return new And(left, right);
        }
    }

    class Or implements TypeMatch {
        private final TypeMatch left;
        private final TypeMatch right;

        private Or(final TypeMatch left, final TypeMatch right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public boolean test(Type type) {
            return left.test(type) || right.test(type);
        }

        public static Or of(final TypeMatch left, final TypeMatch right) {
            return new Or(left, right);
        }
    }
}
