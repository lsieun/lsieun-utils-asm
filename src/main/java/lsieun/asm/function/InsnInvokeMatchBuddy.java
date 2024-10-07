package lsieun.asm.function;


import org.objectweb.asm.Type;

public class InsnInvokeMatchBuddy {
    public static InsnInvokeMatch byReturnType(String desc) {
        return (opcode, owner, name, descriptor) ->
                MethodDescMatch.ByReturnType.of(desc).test(descriptor);
    }

    public static InsnInvokeMatch byReturnType(Class<?> clazz) {
        return (opcode, owner, name, descriptor) ->
                MethodDescMatch.ByReturnType.of(clazz).test(descriptor);
    }

    public static InsnInvokeMatch byReturnType(Type returnType) {
        return (opcode, owner, name, descriptor) ->
                MethodDescMatch.ByReturnType.of(returnType).test(descriptor);
    }
}
