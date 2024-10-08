package lsieun.asm.function.match;

import lsieun.asm.utils.TypeNameUtils;
import lsieun.utils.match.MatchDirection;
import lsieun.utils.match.array.ArrayMatchForMany2One;
import lsieun.utils.match.text.TextMatchForOne2One;
import org.objectweb.asm.Type;

import java.util.Objects;
import java.util.function.Function;

public class TypeMatchBuddy {
    public static TypeMatch byType(String text) {
        Type t = TypeNameUtils.parse(text);
        return byType(t);
    }

    public static TypeMatch byType(Class<?> clazz) {
        Type t = Type.getType(clazz);
        return byType(t);
    }

    public static TypeMatch byType(Type t) {
        return s -> Objects.equals(s, t);
    }

    public static TypeMatch named(String name) {
        return named(TypeNameUtils::toSimpleName, name);
    }

    public static TypeMatch named(Function<Type, String> func, String name) {
        return t -> TextMatchForOne2One.EQUALS_FULLY.test(func.apply(t), name);
    }

    public static TypeMatch namedIgnoreCase(String name) {
        return namedIgnoreCase(TypeNameUtils::toSimpleName, name);
    }

    public static TypeMatch namedIgnoreCase(Function<Type, String> func, String name) {
        return t -> TextMatchForOne2One.EQUALS_FULLY_IGNORE_CASE.test(func.apply(t), name);
    }

    public static TypeMatch nameStartsWith(String prefix) {
        return nameStartsWith(TypeNameUtils::toSimpleName, prefix);
    }

    public static TypeMatch nameStartsWith(Function<Type, String> func, String prefix) {
        return t -> TextMatchForOne2One.STARTS_WITH.test(func.apply(t), prefix);
    }

    public static TypeMatch nameStartsWithIgnoreCase(String prefix) {
        return nameStartsWithIgnoreCase(TypeNameUtils::toSimpleName, prefix);
    }

    public static TypeMatch nameStartsWithIgnoreCase(Function<Type, String> func, String prefix) {
        return t -> TextMatchForOne2One.STARTS_WITH_IGNORE_CASE.test(func.apply(t), prefix);
    }

    public static TypeMatch nameEndsWith(String suffix) {
        return nameEndsWith(TypeNameUtils::toSimpleName, suffix);
    }

    public static TypeMatch nameEndsWith(Function<Type, String> func, String suffix) {
        return t -> TextMatchForOne2One.ENDS_WITH.test(func.apply(t), suffix);
    }

    public static TypeMatch nameEndsWithIgnoreCase(String suffix) {
        return nameEndsWithIgnoreCase(TypeNameUtils::toSimpleName, suffix);
    }

    public static TypeMatch nameEndsWithIgnoreCase(Function<Type, String> func, String suffix) {
        return t -> TextMatchForOne2One.ENDS_WITH_IGNORE_CASE.test(func.apply(t), suffix);
    }

    public static TypeMatch nameContains(String infix) {
        return nameContains(TypeNameUtils::toSimpleName, infix);
    }

    public static TypeMatch nameContains(Function<Type, String> func, String infix) {
        return t -> TextMatchForOne2One.CONTAINS.test(func.apply(t), infix);
    }

    public static TypeMatch nameContainsIgnoreCase(String infix) {
        return nameContainsIgnoreCase(TypeNameUtils::toSimpleName, infix);
    }

    public static TypeMatch nameContainsIgnoreCase(Function<Type, String> func, String infix) {
        return t -> TextMatchForOne2One.CONTAINS_IGNORE_CASE.test(func.apply(t), infix);
    }

    public static TypeMatch nameMatches(String regex) {
        return nameMatches(TypeNameUtils::toSimpleName, regex);
    }

    public static TypeMatch nameMatches(Function<Type, String> func, String regex) {
        return t -> TextMatchForOne2One.REGEX.test(func.apply(t), regex);
    }

    public static TypeMatch namedOneOf(Function<Type, String> func, String... names) {
        ArrayMatchForMany2One<String> match = new ArrayMatchForMany2One<>(
                TextMatchForOne2One.EQUALS_FULLY,
                MatchDirection.BACKWARD
        );
        return t -> match.test(names, func.apply(t));
    }
}
