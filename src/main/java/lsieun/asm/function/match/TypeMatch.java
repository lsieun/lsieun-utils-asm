package lsieun.asm.function.match;

import lsieun.asm.utils.TypeNameUtils;
import org.objectweb.asm.Type;

import java.util.Objects;
import java.util.function.Function;

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

//    class And implements TypeMatch {
//        private final TypeMatch[] matches;
//
//        private And(TypeMatch... matches) {
//            this.matches = matches;
//        }
//
//        @Override
//        public boolean test(Type type) {
//            for (TypeMatch match : matches) {
//                if (!match.test(type)) {
//                    return false;
//                }
//            }
//            return true;
//        }
//
//        public static And of(TypeMatch... matches) {
//            return new And(matches);
//        }
//    }

//    class Or implements TypeMatch {
//        private final TypeMatch[] matches;
//
//        private Or(TypeMatch... matches) {
//            this.matches = matches;
//        }
//
//        @Override
//        public boolean test(Type type) {
//            for (TypeMatch match : matches) {
//                if (match.test(type)) {
//                    return true;
//                }
//            }
//            return false;
//        }
//
//        public static Or of(TypeMatch... matches) {
//            return new Or(matches);
//        }
//    }

    static TypeMatch byType(String text) {
        Type t = TypeNameUtils.parse(text);
        return byType(t);
    }

    static TypeMatch byType(Class<?> clazz) {
        Type t = Type.getType(clazz);
        return byType(t);
    }

    static TypeMatch byType(Type t) {
        return s -> Objects.equals(s, t);
    }

    static TypeMatch bySimpleName(TextMatch textMatch) {
        return byName(TypeNameUtils::toSimpleName, textMatch);
    }

    static TypeMatch byClassName(TextMatch textMatch) {
        return byName(TypeNameUtils::toClassName, textMatch);
    }

    static TypeMatch byInternalName(TextMatch textMatch) {
        return byName(TypeNameUtils::toInternalName, textMatch);
    }

    static TypeMatch byDescriptor(TextMatch textMatch) {
        return byName(TypeNameUtils::toDescriptor, textMatch);
    }

    static TypeMatch byJarEntryName(TextMatch textMatch) {
        return byName(TypeNameUtils::toJarEntryName, textMatch);
    }

    static TypeMatch byName(Function<Type, String> func, TextMatch textMatch) {
        return t -> textMatch.test(func.apply(t));
    }
}
