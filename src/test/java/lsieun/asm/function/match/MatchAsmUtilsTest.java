package lsieun.asm.function.match;

import lsieun.utils.io.file.FileContentUtils;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class MatchAsmUtilsTest {
    @Test
    void testFindSingleAbstractMethod() {
        Class<?> clazz = TextMatch.class;
        Method samMethod = MatchAsmUtils.findSingleAbstractMethod(clazz);
        assertNotNull(samMethod);
        System.out.println(samMethod);
    }

    @Test
    void testGenerateAndSaveEnumTrue() throws IOException {
        Class<?> clazz = TextMatch.class;
        Function<Class<?>, byte[]> func = MatchAsmUtils::generateEnumTrue;
        generateAndSave(clazz, func);
    }

    @Test
    void testGenerateAndSaveEnumFalse() throws IOException {
        Class<?> clazz = TextMatch.class;
        Function<Class<?>, byte[]> func = MatchAsmUtils::generateEnumFalse;
        generateAndSave(clazz, func);
    }

    @Test
    void testGenerateAndSaveLogicAnd() throws IOException {
        Class<?> clazz = TextMatch.class;
        Function<Class<?>, byte[]> func = MatchAsmUtils::generateLogicAnd;
        generateAndSave(clazz, func);
    }

    private void generateAndSave(Class<?> clazz, Function<Class<?>, byte[]> func) throws IOException {
        byte[] bytes = func.apply(clazz);
        assertNotNull(bytes);

        ClassReader cr = new ClassReader(bytes);
        String className = cr.getClassName();
        String entryName = className + ".class";

        Path dir = Path.of(".", "target", "classes");
        Path filePath = dir.resolve(entryName).normalize();
        FileContentUtils.writeBytes(filePath, bytes);
    }

    @Test
    void testGetTrueInstance() {
        MemberMatch memberMatch = MatchAsmUtils.ofTrue(MemberMatch.class);
        assertNotNull(memberMatch);
        System.out.println(memberMatch);
    }

    @Test
    void testGetFalseInstance() {
        MemberMatch memberMatch = MatchAsmUtils.ofFalse(MemberMatch.class);
        assertNotNull(memberMatch);
        System.out.println(memberMatch);
    }

    @Test
    void testAndByReflection() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = Class.forName("lsieun.utils.match.text.TextMatchAnd");
        System.out.println(clazz);
        Method[] declaredMethods = clazz.getDeclaredMethods();
        Method targetMethod = null;
        for (Method declaredMethod : declaredMethods) {
            String name = declaredMethod.getName();
            if (name.equals("of")) {
                targetMethod = declaredMethod;
            }
            System.out.println(declaredMethod);
        }
//        Method method = clazz.getDeclaredMethod("of");
//        System.out.println(method);

        TextMatch[] array = {
                TextMatch.startsWith("You"),
                TextMatch.endsWith("fly")
        };
        assert targetMethod != null;
        Object obj = targetMethod.invoke(null, (Object[]) array);
        System.out.println(obj);
    }

    @Test
    void testAndByMethodInvoke() {
//        String str = "You don't need wings to fly";
//        TextMatch[] array = {
//                TextMatch.startsWith("You"),
//                TextMatch.endsWith("fly")
//        };
//        TextMatch textMatch = MatchAsmUtils.and(TextMatch.class, array);
//        boolean flag = textMatch.test(str);
//        System.out.println(flag);
    }

    @Test
    void testLogicAndTrueUsingTypeMatchByMethodInvoke() {
        Type t = Type.getType(String.class);
        List<TypeMatch> list = List.of(
                TypeMatch.bySimpleName(TextMatch.startsWith("S")),
                TypeMatch.bySimpleName(TextMatch.endsWith("g"))
        );
        TypeMatch typeMatch = MatchAsmUtils.and(TypeMatch.class, list);
        boolean flag = typeMatch.test(t);
        System.out.println(flag);
    }

    @Test
    void testLogicAndFalseUsingTypeMatchByMethodInvoke() {
        Type t = Type.getType(String.class);
        List<TypeMatch> list = List.of(
                TypeMatch.bySimpleName(TextMatch.startsWith("S")),
                TypeMatch.bySimpleName(TextMatch.endsWith("x"))
        );
        TypeMatch typeMatch = MatchAsmUtils.and(TypeMatch.class, list);
        boolean flag = typeMatch.test(t);
        assertFalse(flag);
    }

    @Test
    void testLogicOrTrueUsingTypeMatchByMethodInvoke() {
        Type t = Type.getType(String.class);
        List<TypeMatch> list = List.of(
                TypeMatch.bySimpleName(TextMatch.startsWith("S")),
                TypeMatch.bySimpleName(TextMatch.endsWith("x"))
        );
        TypeMatch typeMatch = MatchAsmUtils.or(TypeMatch.class, list);
        boolean flag = typeMatch.test(t);
        assertTrue(flag);
    }

    @Test
    void testLogicOrFalseUsingTypeMatchByMethodInvoke() {
        Type t = Type.getType(String.class);
        List<TypeMatch> list = List.of(
                TypeMatch.bySimpleName(TextMatch.startsWith("A")),
                TypeMatch.bySimpleName(TextMatch.endsWith("x"))
        );
        TypeMatch typeMatch = MatchAsmUtils.or(TypeMatch.class, list);
        boolean flag = typeMatch.test(t);
        assertFalse(flag);
    }

    @Test
    void testLogicAndTrueUsingTextMatchByMethodInvoke() {
        String str = "You don't need wings to fly";
        List<TextMatch> list = List.of(
                TextMatch.startsWith("You"),
                TextMatch.endsWith("fly")
        );
        MethodHandles.Lookup lookup = TextMatch.lookup();
        TextMatch match = MatchAsmUtils.and(lookup, TextMatch.class, list);
        boolean flag = match.test(str);
        assertTrue(flag);
    }

    @Test
    void testLogicAndFalseUsingTextMatchByMethodInvoke() {
        String str = "You don't need wings to fly";
        List<TextMatch> list = List.of(
                TextMatch.startsWith("You"),
                TextMatch.endsWith("flx")
        );
        MethodHandles.Lookup lookup = TextMatch.lookup();
        TextMatch match = MatchAsmUtils.and(lookup, TextMatch.class, list);
        boolean flag = match.test(str);
        assertFalse(flag);
    }
}