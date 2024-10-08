package lsieun.asm.function.match;


import org.objectweb.asm.Type;

import java.util.Objects;

@SuppressWarnings("UnnecessaryLocalVariable")
public class InsnInvokeMatchBuddy {
    public static InsnInvokeMatch by(String methodName) {
        InsnInvokeMatch match = (opcode, owner, name, descriptor) -> Objects.equals(methodName, name);
        return match;
    }

    public static InsnInvokeMatch byName(TextMatch textMatch) {
        InsnInvokeMatch match = (opcode, owner, name, descriptor) -> textMatch.test(name);
        return match;
    }

    public static InsnInvokeMatch byNameAndDesc(TextMatch textMatch) {
        InsnInvokeMatch match = (opcode, owner, name, descriptor) -> {
            String nameAndDesc = String.format("%s:%s", name, descriptor);
            return textMatch.test(nameAndDesc);
        };
        return match;
    }

    public static InsnInvokeMatch byReturnType(TypeMatch typeMatch) {
        return (opcode, owner, name, descriptor) -> {
            Type returnType = Type.getReturnType(descriptor);
            return typeMatch.test(returnType);
        };
    }
}
