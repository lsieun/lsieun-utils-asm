package lsieun.asm.function.match;

import org.objectweb.asm.Type;

public class MethodDescMatchBuddy {
    public static MethodDescMatch byReturnType(TypeMatch typeMatch) {
        return methodDesc -> {
            Type returnType = Type.getReturnType(methodDesc);
            return typeMatch.test(returnType);
        };
    }
}
