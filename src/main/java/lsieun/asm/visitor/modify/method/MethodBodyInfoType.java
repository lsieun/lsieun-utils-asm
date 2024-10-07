package lsieun.asm.visitor.modify.method;

import java.util.EnumSet;

public enum MethodBodyInfoType {
    ENTER,
    EXIT,
    PARAMETER_VALUES,
    RETURN_VALUE,
    THREAD_INFO,
    CLASSLOADER,
    STACK_TRACE,
    ;

    public static final EnumSet<MethodBodyInfoType> ALL = EnumSet.allOf(MethodBodyInfoType.class);
}
