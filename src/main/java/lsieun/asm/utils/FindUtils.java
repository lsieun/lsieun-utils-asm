package lsieun.asm.utils;

import java.util.ArrayList;
import java.util.List;

import lsieun.asm.visitor.ClassRegexVisitor;
import lsieun.asm.visitor.FindFieldRegexVisitor;
import lsieun.asm.visitor.FindInterfaceRegexVisitor;
import lsieun.asm.visitor.FindMethodRegexVisitor;
import lsieun.asm.visitor.FindSuperRegexVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import lsieun.utils.RegexUtils;
import lsieun.utils.archive.JarUtils;

public class FindUtils {

//    public static List<String> findInterface(String jar_path, String[] path_regex_array, String[] interface_name_regex_array) {
//        FindInterfaceRegexVisitor cv = new FindInterfaceRegexVisitor(interface_name_regex_array, null);
//        return find(jar_path, path_regex_array, cv);
//    }
//
//    public static List<String> findSuper(String jar_path, String[] path_regex_array, String[] super_name_regex_array) {
//        FindSuperRegexVisitor cv = new FindSuperRegexVisitor(super_name_regex_array);
//        return find(jar_path, path_regex_array, cv);
//    }
//
//    public static List<String> findField(String jar_path, String[] path_regex_array, String[] includes, String[] excludes) {
//        FindFieldRegexVisitor cv = new FindFieldRegexVisitor(includes, excludes);
//        return find(jar_path, path_regex_array, cv);
//    }

    /**
     * <b>path_regex_array</b>示例如下：
     * <ul>
     *     <li>部分路径：^com/jetbrains/ls/\\w+/\\w+\\.class$</li>
     *     <li>完整路径：^com/example/abc/xyz/HelloWorld\\.class$  （注意：“.”要进行转义）</li>
     * </ul>
     * <b>name_desc_regex_array</b>的值示例如下：
     * <ul>
     *      <li></li>
     *      <li></li>
     * </ul>
     * <b></b>
     *
     * @param path_regex_array path regex
     * @param includes
     * @param excludes
     */
//    public static List<String> findMethod(String jar_path, String[] path_regex_array, String[] includes, String[] excludes) {
//        FindMethodRegexVisitor cv = new FindMethodRegexVisitor(includes, excludes);
//        return find(jar_path, path_regex_array, cv);
//    }

//    public static List<String> find(String jar_path, String[] path_regex_array, ClassVisitor cv) {
//        List<String> list = JarUtils.getClassEntries(jar_path);
//        RegexUtils.filter(list, path_regex_array);
//
//        List<String> resultList = new ArrayList<>();
//
//        for (String str : list) {
//            byte[] bytes = JarUtils.readClass(jar_path, str);
//            ClassReader cr = new ClassReader(bytes);
//            cr.accept(cv, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
//            if (cv instanceof ClassRegexVisitor) {
//                ClassRegexVisitor ra = (ClassRegexVisitor) cv;
//                if (ra.gotcha) {
//                    resultList.add(ra.result);
//                }
//            }
//        }
//        return resultList;
//    }

//    public static List<String> findOpcode(String jar_path, String[] path_regex_array, ClassVisitor cv) {
//        List<String> list = JarUtils.getClassEntries(jar_path);
//        RegexUtils.filter(list, path_regex_array);
//
//        List<String> resultList = new ArrayList<>();
//
//        for (String str : list) {
//            byte[] bytes = JarUtils.readClass(jar_path, str);
//            ClassReader cr = new ClassReader(bytes);
//            cr.accept(cv, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
//            if (cv instanceof ClassRegexVisitor) {
//                ClassRegexVisitor ra = (ClassRegexVisitor) cv;
//                if (ra.gotcha) {
//                    resultList.add(ra.result);
//                }
//            }
//        }
//        return resultList;
//    }

//    public static void displayResult(List<String> resultList) {
//        if (resultList == null || resultList.size() < 1) return;
//
//        for (int i = 0; i < resultList.size(); i++) {
//            Result result = resultList.get(i);
//            System.out.println(String.format("(%s)ClassName: %s", (i + 1), result.className));
//            for (Result.NameAndDesc item : result.list) {
//                System.out.println(String.format("%s: %s", item.name, item.desc));
//            }
//            System.out.println();
//        }
//    }
}
