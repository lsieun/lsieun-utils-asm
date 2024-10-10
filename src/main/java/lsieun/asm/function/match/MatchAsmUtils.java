package lsieun.asm.function.match;

import lsieun.asm.utils.TypeBuddy;
import lsieun.utils.log.Logger;
import lsieun.utils.log.LoggerFactory;
import org.objectweb.asm.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static org.objectweb.asm.Opcodes.*;

public class MatchAsmUtils {
    private static final Logger logger = LoggerFactory.getLogger(MatchAsmUtils.class);

    private static final String FACTORY_METHOD_NAME = "of";
    private static final String INSTANCE_FIELD = "INSTANCE";
    private static final int CLASS_WRITER_FLAGS = ClassWriter.COMPUTE_FRAMES;
    private static final Map<Class<?>, Class<?>> trueMap = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Class<?>> falseMap = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Class<?>> andMap = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Class<?>> orMap = new ConcurrentHashMap<>();


    public static <T> T ofTrue(Class<T> samClass) {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        return ofTrue(lookup, samClass);
    }

    public static <T> T ofTrue(MethodHandles.Lookup lookup, Class<T> samClass) {
        checkFunctionalInterface(samClass);
        Class<?> clazz = getClass(lookup, samClass, trueMap, MatchAsmUtils::generateEnumTrue);
        if (clazz == null) {
            return null;
        }

        return getInstance(lookup, samClass, clazz);
    }

    public static <T> T ofFalse(Class<T> samClass) {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        return ofFalse(lookup, samClass);
    }

    public static <T> T ofFalse(MethodHandles.Lookup lookup, Class<T> samClass) {
        checkFunctionalInterface(samClass);
        Class<?> clazz = getClass(lookup, samClass, falseMap, MatchAsmUtils::generateEnumFalse);
        if (clazz == null) {
            return null;
        }

        return getInstance(lookup, samClass, clazz);
    }

    public static <T> T and(Class<T> samClass, List<T> list) {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        return and(lookup, samClass, list);
    }

    public static <T> T and(MethodHandles.Lookup lookup, Class<T> samClass, List<T> list) {
        checkFunctionalInterface(samClass);
        Class<?> clazz = getClass(lookup, samClass, andMap, MatchAsmUtils::generateLogicAnd);
        if (clazz == null) {
            return null;
        }

        return invokeOfMethod(lookup, samClass, clazz, list);
    }

    public static <T> T or(Class<T> samClass, List<T> list) {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        return or(lookup, samClass, list);
    }

    public static <T> T or(MethodHandles.Lookup lookup, Class<T> samClass, List<T> list) {
        checkFunctionalInterface(samClass);
        Class<?> clazz = getClass(lookup, samClass, orMap, MatchAsmUtils::generateLogicOr);
        if (clazz == null) {
            return null;
        }

        return invokeOfMethod(lookup, samClass, clazz, list);
    }

    private static Class<?> getClass(MethodHandles.Lookup lookup, Class<?> samClass, Map<Class<?>, Class<?>> map, Function<Class<?>, byte[]> func) {
        checkFunctionalInterface(samClass);

        return map.computeIfAbsent(samClass, k -> {
            try {
                byte[] bytes = func.apply(samClass);
                return lookup.defineClass(bytes);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private static <T> T getInstance(MethodHandles.Lookup lookup, Class<T> samClass, Class<?> enumClazz) {
        try {
            VarHandle varHandle = lookup.in(enumClazz)
                    .findStaticVarHandle(enumClazz, INSTANCE_FIELD, enumClazz);
            Object result = varHandle.get();
            if (samClass.isInstance(result)) {
                return samClass.cast(result);
            }
            else {
                String msg = String.format(
                        "samClass = %s, clazz = %s, result = %s",
                        samClass.getName(),
                        enumClazz.getName(),
                        result
                );
                throw new RuntimeException(msg);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static <T> T invokeOfMethod(MethodHandles.Lookup lookup, Class<T> samClass, Class<?> clazz, List<T> list) {
        try {
            Method targetMethod = null;
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                if (declaredMethod.getName().equals(FACTORY_METHOD_NAME)) {
                    targetMethod = declaredMethod;
                }
            }

            if (targetMethod != null) {
                MethodHandle methodHandle = lookup.unreflect(targetMethod);
                Object result = methodHandle.invoke(list);
                if (samClass.isInstance(result)) {
                    return samClass.cast(result);
                }
                else {
                    String msg = String.format(
                            "samClass = %s, clazz = %s, result = %s",
                            samClass.getName(),
                            clazz.getName(),
                            result
                    );
                    throw new RuntimeException(msg);
                }
            }
            else {
                throw new RuntimeException("invokeOfMethod(): targetMethod is null");
            }


        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }


    // region Class -> byte[]
    static byte[] generateEnumTrue(Class<?> samClass) {
        return generateEnumBool(samClass, true);
    }

    static byte[] generateEnumFalse(Class<?> samClass) {
        return generateEnumBool(samClass, false);
    }

    static byte[] generateEnumBool(Class<?> samClass, boolean flag) {
        Method samMethod = findSingleAbstractMethod(samClass);

        Type t = Type.getType(samClass);
        String methodName = samMethod.getName();
        Type methodType = Type.getType(samMethod);
        return generateEnumBool(t, methodName, methodType, flag);
    }

    static byte[] generateEnumBool(Type samType, String samMethodName, Type samMethodType, boolean flag) {
        Type newClassType = flag ? getTrueType(samType) : getFalseType(samType);


        Type newClassArrayType = TypeBuddy.toArray(newClassType, 1);
        Type void2NewClassArrayMethodType = Type.getMethodType(newClassArrayType);
        String innerClassSignature = String.format("Ljava/lang/Enum<%s>;%s", newClassType.getDescriptor(), samType.getDescriptor());

        ClassWriter cw = new ClassWriter(CLASS_WRITER_FLAGS);

        cw.visit(V17, ACC_PUBLIC | ACC_FINAL | ACC_SUPER | ACC_ENUM, newClassType.getInternalName(),
                innerClassSignature,
                "java/lang/Enum",
                new String[]{samType.getInternalName()});

        // field: INSTANCE
        {
            FieldVisitor fieldVisitor = cw.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC | ACC_ENUM,
                    INSTANCE_FIELD, newClassType.getDescriptor(), null, null);
            fieldVisitor.visitEnd();
        }
        // field: $VALUES
        {
            FieldVisitor fieldVisitor = cw.visitField(ACC_PRIVATE | ACC_FINAL | ACC_STATIC | ACC_SYNTHETIC,
                    "$VALUES", newClassArrayType.getDescriptor(), null, null);
            fieldVisitor.visitEnd();
        }
        // method: values()
        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC | ACC_STATIC,
                    "values", void2NewClassArrayMethodType.getDescriptor(), null, null);
            mv.visitCode();
            mv.visitFieldInsn(GETSTATIC, newClassType.getInternalName(), "$VALUES", newClassArrayType.getDescriptor());
            mv.visitMethodInsn(INVOKEVIRTUAL, newClassArrayType.getDescriptor(), "clone", "()Ljava/lang/Object;", false);
            mv.visitTypeInsn(CHECKCAST, newClassArrayType.getDescriptor());
            mv.visitInsn(ARETURN);
            mv.visitMaxs(1, 0);
            mv.visitEnd();
        }
        // method: valueOf(String)
        {
            Type valueOfMethodType = Type.getMethodType(newClassType, Type.getType(String.class));
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC | ACC_STATIC,
                    "valueOf", valueOfMethodType.getDescriptor(), null, null);
            mv.visitCode();
            mv.visitLdcInsn(Type.getType(newClassType.getDescriptor()));
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Enum",
                    "valueOf", "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;", false);
            mv.visitTypeInsn(CHECKCAST, newClassType.getInternalName());
            mv.visitInsn(ARETURN);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        // constructor: <init>
        {
            MethodVisitor mv = cw.visitMethod(ACC_PRIVATE,
                    "<init>", "(Ljava/lang/String;I)V", "()V", null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ILOAD, 2);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Enum", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(3, 3);
            mv.visitEnd();
        }
        // method: single abstract method
        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC,
                    samMethodName, samMethodType.getDescriptor(), null, null);
            mv.visitCode();
            mv.visitInsn(flag ? ICONST_1 : ICONST_0);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(1, 5);
            mv.visitEnd();
        }
        // method: $values()
        {
            MethodVisitor mv = cw.visitMethod(
                    ACC_PRIVATE | ACC_STATIC | ACC_SYNTHETIC,
                    "$values", void2NewClassArrayMethodType.getDescriptor(), null, null);
            mv.visitCode();
            mv.visitInsn(ICONST_1);
            mv.visitTypeInsn(ANEWARRAY, newClassType.getInternalName());
            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_0);
            mv.visitFieldInsn(GETSTATIC, newClassType.getInternalName(), INSTANCE_FIELD, newClassType.getDescriptor());
            mv.visitInsn(AASTORE);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(4, 0);
            mv.visitEnd();
        }
        // method: <clinit>
        {
            MethodVisitor mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            mv.visitCode();
            mv.visitTypeInsn(NEW, newClassType.getInternalName());
            mv.visitInsn(DUP);
            mv.visitLdcInsn(INSTANCE_FIELD);
            mv.visitInsn(ICONST_0);
            mv.visitMethodInsn(INVOKESPECIAL, newClassType.getInternalName(), "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitFieldInsn(PUTSTATIC, newClassType.getInternalName(), INSTANCE_FIELD, newClassType.getDescriptor());
            mv.visitMethodInsn(INVOKESTATIC, newClassType.getInternalName(), "$values", void2NewClassArrayMethodType.getDescriptor(), false);
            mv.visitFieldInsn(PUTSTATIC, newClassType.getInternalName(), "$VALUES", newClassArrayType.getDescriptor());
            mv.visitInsn(RETURN);
            mv.visitMaxs(4, 0);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }

    static byte[] generateLogicAnd(Class<?> samClass) {
        Method samMethod = findSingleAbstractMethod(samClass);

        Type t = Type.getType(samClass);
        String methodName = samMethod.getName();
        Type methodType = Type.getType(samMethod);
        return generateLogicAnd(t, methodName, methodType);
    }

    static byte[] generateLogicAnd(Type samType, String samMethodName, Type samMethodType) {
        Type newClassType = getAndType(samType);
        ClassWriter cw = new ClassWriter(CLASS_WRITER_FLAGS);

        cw.visit(V17, ACC_PUBLIC | ACC_SUPER,
                newClassType.getInternalName(), null, "java/lang/Object",
                new String[]{samType.getInternalName()});

        // field: matchList
        {
            String matchListSignature = String.format("Ljava/util/List<%s>;", samType.getDescriptor());
            FieldVisitor fv = cw.visitField(
                    ACC_PRIVATE | ACC_FINAL,
                    "matchList",
                    "Ljava/util/List;",
                    matchListSignature, null);
            fv.visitEnd();
        }
        // constructor: <init>
        {
            String constructorSignature = String.format("(Ljava/util/List<%s>;)V", samType.getDescriptor());
            MethodVisitor mv = cw.visitMethod(ACC_PRIVATE, "<init>", "(Ljava/util/List;)V",
                    constructorSignature, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(PUTFIELD, newClassType.getInternalName(), "matchList", "Ljava/util/List;");
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        // method: single abstract method
        {
            Type[] argumentTypes = samMethodType.getArgumentTypes();
            int freeSlotIndex = 1;
            for (Type argumentType : argumentTypes) {
                freeSlotIndex += argumentType.getSize();
            }

            int slotIndexForSize = freeSlotIndex++;
            int slotIndexForI = freeSlotIndex++;
            int slotIndexForObj = freeSlotIndex++;
            int maxLocals = freeSlotIndex++;

            Label forLoopStartLabel = new Label();
            Label forContinueLabel = new Label();
            Label forStopLabel = new Label();

            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, samMethodName, samMethodType.getDescriptor(), null, null);
            mv.visitCode();

            // int size = matchList.size();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, newClassType.getInternalName(), "matchList", "Ljava/util/List;");
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "size", "()I", true);
            mv.visitVarInsn(ISTORE, slotIndexForSize);

            // int i = 0;
            mv.visitInsn(ICONST_0);
            mv.visitVarInsn(ISTORE, slotIndexForI);

            // i < size
            mv.visitLabel(forLoopStartLabel);
            mv.visitVarInsn(ILOAD, slotIndexForI);
            mv.visitVarInsn(ILOAD, slotIndexForSize);
            mv.visitJumpInsn(IF_ICMPGE, forStopLabel);

            // obj = matchList.get(i);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, newClassType.getInternalName(), "matchList", "Ljava/util/List;");
            mv.visitVarInsn(ILOAD, slotIndexForI);
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "get", "(I)Ljava/lang/Object;", true);
            mv.visitTypeInsn(CHECKCAST, samType.getInternalName());
            mv.visitVarInsn(ASTORE, slotIndexForObj);
            mv.visitVarInsn(ALOAD, slotIndexForObj);

            // flag = obj.test(...)
            int slotIndex = 1;
            for (Type argumentType : argumentTypes) {
                // load variable
                int opcode = argumentType.getOpcode(Opcodes.ILOAD);
                mv.visitVarInsn(opcode, slotIndex);

                slotIndex += argumentType.getSize();
            }
            mv.visitMethodInsn(INVOKEINTERFACE, samType.getInternalName(), samMethodName,
                    samMethodType.getDescriptor(), true);

            // flag == true or false
            mv.visitJumpInsn(IFNE, forContinueLabel);
            mv.visitInsn(ICONST_0);
            mv.visitInsn(IRETURN);

            // i++
            mv.visitLabel(forContinueLabel);
            mv.visitIincInsn(slotIndexForI, 1);
            mv.visitJumpInsn(GOTO, forLoopStartLabel);

            // for-stop
            mv.visitLabel(forStopLabel);
            mv.visitInsn(ICONST_1);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(5, maxLocals);
            mv.visitEnd();
        }
        // method: of
        {
            Type ofMethodDescriptor = Type.getMethodType(samType, Type.getType(List.class));
            String ofMethodSignature = String.format("(Ljava/util/List<%1$s>;)%1$s", samType.getDescriptor());
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC | ACC_STATIC,
                    FACTORY_METHOD_NAME, ofMethodDescriptor.getDescriptor(),
                    ofMethodSignature, null);
            mv.visitCode();
            mv.visitTypeInsn(NEW, newClassType.getInternalName());
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, newClassType.getInternalName(), "<init>", "(Ljava/util/List;)V", false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(3, 1);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }

    static byte[] generateLogicOr(Class<?> samClass) {
        Method samMethod = findSingleAbstractMethod(samClass);

        Type t = Type.getType(samClass);
        String methodName = samMethod.getName();
        Type methodType = Type.getType(samMethod);
        return generateLogicOr(t, methodName, methodType);
    }

    static byte[] generateLogicOr(Type samType, String samMethodName, Type samMethodType) {
        Type newClassType = getOrType(samType);

        ClassWriter cw = new ClassWriter(CLASS_WRITER_FLAGS);

        cw.visit(V17, ACC_PUBLIC | ACC_SUPER,
                newClassType.getInternalName(), null, "java/lang/Object",
                new String[]{samType.getInternalName()});

        // field: matchList
        {
            String matchListSignature = String.format("Ljava/util/List<%s>;", samType.getDescriptor());
            FieldVisitor fv = cw.visitField(ACC_PRIVATE | ACC_FINAL,
                    "matchList", "Ljava/util/List;", matchListSignature, null);
            fv.visitEnd();
        }
        // constructor: <init>
        {
            String constructorSignature = String.format("(Ljava/util/List<%s>;)V", samType.getDescriptor());
            MethodVisitor mv = cw.visitMethod(ACC_PRIVATE, "<init>", "(Ljava/util/List;)V",
                    constructorSignature, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(PUTFIELD, newClassType.getInternalName(), "matchList", "Ljava/util/List;");
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        // method: single abstract method
        {
            Type[] argumentTypes = samMethodType.getArgumentTypes();
            int freeSlotIndex = 1;
            for (Type argumentType : argumentTypes) {
                freeSlotIndex += argumentType.getSize();
            }

            int slotIndexForSize = freeSlotIndex++;
            int slotIndexForI = freeSlotIndex++;
            int slotIndexForObj = freeSlotIndex++;
            int maxLocals = freeSlotIndex++;

            Label forStartLabel = new Label();
            Label forContinueLabel = new Label();
            Label forStopLabel = new Label();

            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, samMethodName, samMethodType.getDescriptor(), null, null);
            mv.visitCode();

            // int size = matchList.size();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, newClassType.getInternalName(), "matchList", "Ljava/util/List;");
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "size", "()I", true);
            mv.visitVarInsn(ISTORE, slotIndexForSize);

            // int i = 0;
            mv.visitInsn(ICONST_0);
            mv.visitVarInsn(ISTORE, slotIndexForI);

            // i < size
            mv.visitLabel(forStartLabel);
            mv.visitVarInsn(ILOAD, slotIndexForI);
            mv.visitVarInsn(ILOAD, slotIndexForSize);
            mv.visitJumpInsn(IF_ICMPGE, forStopLabel);

            // obj = matchList.get(i);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, "lsieun/asm/function/match/InsnInvokeMatchOr", "matchList", "Ljava/util/List;");
            mv.visitVarInsn(ILOAD, slotIndexForI);
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "get", "(I)Ljava/lang/Object;", true);
            mv.visitTypeInsn(CHECKCAST, "lsieun/asm/function/match/InsnInvokeMatch");
            mv.visitVarInsn(ASTORE, slotIndexForObj);
            mv.visitVarInsn(ALOAD, slotIndexForObj);

            // flag = obj.test(...)
            int slotIndex = 1;
            for (Type argumentType : argumentTypes) {
                // load variable
                int opcode = argumentType.getOpcode(Opcodes.ILOAD);
                mv.visitVarInsn(opcode, slotIndex);

                slotIndex += argumentType.getSize();
            }
            mv.visitMethodInsn(INVOKEINTERFACE, samType.getInternalName(),
                    samMethodName, samMethodType.getDescriptor(), true);

            // flag == true or false
            mv.visitJumpInsn(IFEQ, forContinueLabel);
            mv.visitInsn(ICONST_1);
            mv.visitInsn(IRETURN);

            // i++
            mv.visitLabel(forContinueLabel);
            mv.visitIincInsn(slotIndexForI, 1);
            mv.visitJumpInsn(GOTO, forStartLabel);

            // for-stop
            mv.visitLabel(forStopLabel);
            mv.visitInsn(ICONST_0);
            mv.visitInsn(IRETURN);

            mv.visitMaxs(5, maxLocals);
            mv.visitEnd();
        }
        // method: of
        {
            Type ofMethodDescriptor = Type.getMethodType(samType, Type.getType(List.class));
            String ofMethodSignature = String.format("(Ljava/util/List<%1$s>;)%1$s", samType.getDescriptor());
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC | ACC_STATIC,
                    FACTORY_METHOD_NAME, ofMethodDescriptor.getDescriptor(),
                    ofMethodSignature, null);
            mv.visitCode();
            mv.visitTypeInsn(NEW, newClassType.getInternalName());
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, newClassType.getInternalName(), "<init>", "(Ljava/util/List;)V", false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(3, 1);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }

    static Method findSingleAbstractMethod(Class<?> clazz) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            int modifiers = method.getModifiers();
            if (Modifier.isAbstract(modifiers)) {
                return method;
            }
        }
        throw new IllegalArgumentException("No method found for " + clazz);
    }

    static Type getTrueType(Type t) {
        return getNewType(t, "True");
    }

    static Type getFalseType(Type t) {
        return getNewType(t, "False");
    }

    static Type getAndType(Type t) {
        return getNewType(t, "And");
    }

    static Type getOrType(Type t) {
        return getNewType(t, "Or");
    }

    static Type getNewType(Type t, String suffix) {
        String internalName = t.getInternalName();
        String newInterfaceName = String.format("%s%s", internalName, suffix);
        return Type.getObjectType(newInterfaceName);
    }
    // endregion


    // region check
    private static void checkFunctionalInterface(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        if (!clazz.isInterface() || clazz.getAnnotation(FunctionalInterface.class) == null) {
            String msg = String.format("The class %s is not a functional interface", clazz.getTypeName());
            throw new IllegalArgumentException(msg);
        }
    }
    // endregion
}
