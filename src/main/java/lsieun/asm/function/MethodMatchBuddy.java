package lsieun.asm.function;

import org.objectweb.asm.Type;

@SuppressWarnings("UnnecessaryLocalVariable")
public class MethodMatchBuddy {
    public static MethodMatch by(String name, String desc) {
        MethodMatch match = (version, owner, methodAccess, methodName, methodDesc, signature, exceptions) ->
                methodName.equals(name) && methodDesc.equals(desc);
        return match;
    }

    public static MethodMatch by(String internalClassName, String name, String desc) {
        MethodMatch match = (version, owner, methodAccess, methodName, methodDesc, signature, exceptions) ->
                owner.equals(internalClassName) && methodName.equals(name) && methodDesc.equals(desc);
        return match;
    }

    public static MethodMatch byReturnType(String desc) {
        return  (version, owner, methodAccess, methodName, methodDesc, signature, exceptions) ->
                MethodDescMatch.ByReturnType.of(desc).test(methodDesc);
    }

    public static MethodMatch byReturnType(Class<?> clazz) {
        return (version, owner, methodAccess, methodName, methodDesc, signature, exceptions) ->
                MethodDescMatch.ByReturnType.of(clazz).test(methodDesc);
    }

    public static MethodMatch byReturnType(Type returnType) {
        return (version, owner, methodAccess, methodName, methodDesc, signature, exceptions) ->
                MethodDescMatch.ByReturnType.of(returnType).test(methodDesc);
    }
}
