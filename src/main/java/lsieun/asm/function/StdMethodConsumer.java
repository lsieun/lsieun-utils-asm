package lsieun.asm.function;

import lsieun.asm.utils.CodeSegmentUtils;
import lsieun.utils.log.LogColor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public enum StdMethodConsumer implements MethodConsumer, Opcodes {
    ENTER {
        @Override
        public void accept(MethodVisitor mv, int version, String owner, int methodAccess, String methodName, String methodDesc) {
            String line = String.format("%s::%s:%s %s", owner, methodName, methodDesc, ">".repeat(81));
            CodeSegmentUtils.printMessage(mv, line);
        }
    },
    EXIT {
        @Override
        public void accept(MethodVisitor mv, int version, String owner, int methodAccess, String methodName, String methodDesc) {
            String line = String.format("%s::%s:%s %s", owner, methodName, methodDesc, "<".repeat(81));
            CodeSegmentUtils.printMessage(mv, line);
        }
    },
    PARAMETER_VALUES {
        @Override
        public void accept(MethodVisitor mv, int version, String owner, int methodAccess, String methodName, String methodDesc) {
            CodeSegmentUtils.printParameters(mv, methodAccess, methodDesc);
        }
    },
    RETURN_VOID {
        @Override
        public void accept(MethodVisitor mv, int version, String owner, int methodAccess, String methodName, String methodDesc) {
            String line = String.format("Method Return: %s.%s:%s", owner, methodName, methodDesc);
            CodeSegmentUtils.printMessage(mv, line);
            String message = "    return void";
            CodeSegmentUtils.printMessage(mv, message);
        }
    },
    RETURN_VALUE {
        @Override
        public void accept(MethodVisitor mv, int version, String owner, int methodAccess, String methodName, String methodDesc) {
            CodeSegmentUtils.printReturnValue(mv, owner, methodName, methodDesc);
        }
    },
    THROWN {
        @Override
        public void accept(MethodVisitor mv, int version, String owner, int methodAccess, String methodName, String methodDesc) {
            String line = String.format("Method throws Exception: %s.%s:%s", owner, methodName, methodDesc);
            CodeSegmentUtils.printMessage(mv, line);
            String message = "    abnormal return";
            CodeSegmentUtils.printMessage(mv, message);
        }
    },
    CLASSLOADER {
        @Override
        public void accept(MethodVisitor mv, int version, String owner, int methodAccess, String methodName, String methodDesc) {
            CodeSegmentUtils.printClassLoader(mv, owner);
        }
    },
    STACK_TRACE {
        @Override
        public void accept(MethodVisitor mv, int version, String owner, int methodAccess, String methodName, String methodDesc) {
            CodeSegmentUtils.printMessage(mv, "Stack Trace:");
            if (version < (44 + 9)) {
                CodeSegmentUtils.printStackTrace(mv, owner);
            }
            else {
                CodeSegmentUtils.printStackTraceSinceJava9(mv, owner);
            }
        }
    },
    THREAD_INFO {
        @Override
        public void accept(MethodVisitor mv, int version, String owner, int methodAccess, String methodName, String methodDesc) {
            CodeSegmentUtils.printThreadInfo(mv);
        }
    };
}
