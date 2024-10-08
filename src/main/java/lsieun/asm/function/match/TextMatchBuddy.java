package lsieun.asm.function.match;

import lsieun.utils.match.MatchDirection;
import lsieun.utils.match.array.ArrayMatchForMany2One;
import lsieun.utils.match.text.TextMatchForOne2One;

public class TextMatchBuddy {
    public static TextMatch equals(String text) {
        return str -> TextMatchForOne2One.EQUALS_FULLY.test(str, text);
    }

    public static TextMatch equalsIgnoreCase(String text) {
        return str -> TextMatchForOne2One.EQUALS_FULLY_IGNORE_CASE.test(str, text);
    }

    public static TextMatch startsWith(String prefix) {
        return str -> TextMatchForOne2One.STARTS_WITH.test(str, prefix);
    }

    public static TextMatch startsWithIgnoreCase(String prefix) {
        return str -> TextMatchForOne2One.STARTS_WITH_IGNORE_CASE.test(str, prefix);
    }

    public static TextMatch endsWith(String suffix) {
        return str -> TextMatchForOne2One.ENDS_WITH.test(str, suffix);
    }

    public static TextMatch endsWithIgnoreCase(String suffix) {
        return str -> TextMatchForOne2One.ENDS_WITH_IGNORE_CASE.test(str, suffix);
    }

    public static TextMatch contains(String infix) {
        return str -> TextMatchForOne2One.CONTAINS.test(str, infix);
    }

    public static TextMatch containsIgnoreCase(String infix) {
        return str -> TextMatchForOne2One.CONTAINS_IGNORE_CASE.test(str, infix);
    }

    public static TextMatch matches(String regex) {
        return str -> TextMatchForOne2One.REGEX.test(str, regex);
    }

    public static TextMatch oneOf(String... textArray) {
        ArrayMatchForMany2One<String> match = new ArrayMatchForMany2One<>(
                TextMatchForOne2One.EQUALS_FULLY,
                MatchDirection.BACKWARD
        );
        return str -> match.test(textArray, str);
    }
}
