package lsieun.asm.function;

import lsieun.asm.utils.TypeNameUtils;
import org.objectweb.asm.Type;

public interface MethodDescMatch {
    boolean test(String methodDesc);

    class ByReturnType implements MethodDescMatch {
        private final Type returnType;

        private ByReturnType(Type returnType) {
            this.returnType = returnType;
        }

        @Override
        public boolean test(String methodDesc) {
            Type t = Type.getMethodType(methodDesc);
            return returnType.equals(t.getReturnType());
        }

        public static ByReturnType of(Type returnType) {
            return new ByReturnType(returnType);
        }

        public static ByReturnType of(String text) {
            Type returnType = TypeNameUtils.parse(text);
            return of(returnType);
        }

        public static ByReturnType of(Class<?> clazz) {
            Type returnType = Type.getType(clazz);
            return of(returnType);
        }
    }
}
