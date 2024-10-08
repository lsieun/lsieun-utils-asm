package lsieun.asm.function.match;

import org.objectweb.asm.Type;

@SuppressWarnings("UnnecessaryLocalVariable")
public class MethodMatchBuddy {
    public static MethodMatch by(String name) {
        MethodMatch match = (version, owner, methodAccess, methodName, methodDesc, signature, exceptions) ->
                methodName.equals(name);
        return match;
    }

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

    public static MethodMatch byName(TextMatch textMatch) {
        MethodMatch match = (version, owner, methodAccess, methodName, methodDesc, signature, exceptions) ->
                textMatch.test(methodName);
        return match;
    }

    public static MethodMatch byNameAndDesc(TextMatch textMatch) {
        MethodMatch match = (version, owner, methodAccess, methodName, methodDesc, signature, exceptions) ->
        {
            String nameAndDesc = String.format("%s:%s", methodName, methodDesc);
            return textMatch.test(nameAndDesc);
        };
        return match;
    }

    public static MethodMatch byReturnType(TypeMatch typeMatch) {
        return (version, owner, methodAccess, methodName, methodDesc, signature, exceptions) ->
        {
            Type returnType = Type.getReturnType(methodDesc);
            return typeMatch.test(returnType);
        };
    }
}
