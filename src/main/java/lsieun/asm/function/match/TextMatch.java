package lsieun.asm.function.match;

/**
 * @see TextMatchBuddy
 */
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
}
