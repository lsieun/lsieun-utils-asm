package lsieun.asm.function.match;

import lsieun.utils.match.MatchDirection;
import lsieun.utils.match.array.ArrayMatchForMany2One;
import lsieun.utils.match.text.TextMatchForOne2One;

import java.lang.invoke.MethodHandles;


@FunctionalInterface
public interface TextMatch {
    boolean test(String text);

    enum Bool implements TextMatch {
        TRUE {
            @Override
            public boolean test(String text) {
                return true;
            }
        },
        FALSE {
            @Override
            public boolean test(String text) {
                return false;
            }
        };
    }

    class And implements TextMatch {
        private final TextMatch[] matches;

        private And(final TextMatch... matches) {
            this.matches = matches;
        }

        @Override
        public boolean test(String text) {
            for (final TextMatch match : matches) {
                if (!match.test(text)) {
                    return false;
                }
            }
            return true;
        }
    }

    class Or implements TextMatch {
        private final TextMatch[] matches;

        private Or(final TextMatch... matches) {
            this.matches = matches;
        }

        @Override
        public boolean test(String text) {
            for (final TextMatch match : matches) {
                if (match.test(text)) {
                    return true;
                }
            }
            return false;
        }
    }

    static TextMatch equals(String text) {
        return str -> TextMatchForOne2One.EQUALS_FULLY.test(str, text);
    }

    static TextMatch equalsIgnoreCase(String text) {
        return str -> TextMatchForOne2One.EQUALS_FULLY_IGNORE_CASE.test(str, text);
    }

    static TextMatch startsWith(String prefix) {
        return str -> TextMatchForOne2One.STARTS_WITH.test(str, prefix);
    }

    static TextMatch startsWithIgnoreCase(String prefix) {
        return str -> TextMatchForOne2One.STARTS_WITH_IGNORE_CASE.test(str, prefix);
    }

    static TextMatch endsWith(String suffix) {
        return str -> TextMatchForOne2One.ENDS_WITH.test(str, suffix);
    }

    static TextMatch endsWithIgnoreCase(String suffix) {
        return str -> TextMatchForOne2One.ENDS_WITH_IGNORE_CASE.test(str, suffix);
    }

    static TextMatch contains(String infix) {
        return str -> TextMatchForOne2One.CONTAINS.test(str, infix);
    }

    static TextMatch containsIgnoreCase(String infix) {
        return str -> TextMatchForOne2One.CONTAINS_IGNORE_CASE.test(str, infix);
    }

    static TextMatch matches(String regex) {
        return str -> TextMatchForOne2One.REGEX.test(str, regex);
    }

    static TextMatch oneOf(String... textArray) {
        ArrayMatchForMany2One<String> match = new ArrayMatchForMany2One<>(
                TextMatchForOne2One.EQUALS_FULLY,
                MatchDirection.BACKWARD
        );
        return str -> match.test(textArray, str);
    }

    static MethodHandles.Lookup lookup() {
        return MethodHandles.lookup();
    }
}
