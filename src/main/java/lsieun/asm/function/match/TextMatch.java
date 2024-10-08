package lsieun.asm.function.match;

/**
 * @see TextMatchBuddy
 */
@FunctionalInterface
public interface TextMatch {
    boolean test(String text);
}
