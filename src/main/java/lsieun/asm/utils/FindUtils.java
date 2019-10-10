package lsieun.asm.utils;

import java.util.ArrayList;
import java.util.List;

import lsieun.asm.adapter.RegexAdapter;
import lsieun.asm.adapter.find.*;
import lsieun.asm.adapter.find.clazz.FindFieldAdapter;
import lsieun.asm.adapter.find.clazz.FindInterfaceAdapter;
import lsieun.asm.adapter.find.clazz.FindMethodAdaptor;
import lsieun.asm.adapter.find.clazz.FindSuperAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import lsieun.utils.RegexUtils;
import lsieun.utils.archive.JarUtils;

public class FindUtils {

    public static List<Result> findInterface(String jar_path, String[] path_regex_array, String[] interface_name_regex_array) {
        FindInterfaceAdapter cv = new FindInterfaceAdapter(null, interface_name_regex_array);
        return find(jar_path, path_regex_array, cv);
    }

    public static List<Result> findSuper(String jar_path, String[] path_regex_array, String[] super_name_regex_array) {
        FindSuperAdapter cv = new FindSuperAdapter(null, super_name_regex_array);
        return find(jar_path, path_regex_array, cv);
    }

    public static List<Result> findField(String jar_path, String[] path_regex_array, String[] includes, String[] excludes) {
        FindFieldAdapter cv = new FindFieldAdapter(null, includes, excludes);
        return find(jar_path, path_regex_array, cv);
    }

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
    public static List<Result> findMethod(String jar_path, String[] path_regex_array, String[] includes, String[] excludes) {
        FindMethodAdaptor cv = new FindMethodAdaptor(null, includes, excludes);
        return find(jar_path, path_regex_array, cv);
    }

    public static List<Result> find(String jar_path, String[] path_regex_array, ClassVisitor cv) {
        List<String> list = JarUtils.getClassEntries(jar_path);
        RegexUtils.filter(list, path_regex_array);

        List<Result> resultList = new ArrayList<>();

        for (String str : list) {
            byte[] bytes = JarUtils.readClass(jar_path, str);
            ClassReader cr = new ClassReader(bytes);
            cr.accept(cv, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            if (cv instanceof RegexAdapter) {
                RegexAdapter ra = (RegexAdapter) cv;
                if (ra.gotcha) {
                    resultList.add(ra.result);
                }
            }
        }
        return resultList;
    }

    public static List<Result> findOpcode(String jar_path, String[] path_regex_array, ClassVisitor cv) {
        List<String> list = JarUtils.getClassEntries(jar_path);
        RegexUtils.filter(list, path_regex_array);

        List<Result> resultList = new ArrayList<>();

        for (String str : list) {
            byte[] bytes = JarUtils.readClass(jar_path, str);
            ClassReader cr = new ClassReader(bytes);
            cr.accept(cv, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            if (cv instanceof RegexAdapter) {
                RegexAdapter ra = (RegexAdapter) cv;
                if (ra.gotcha) {
                    resultList.add(ra.result);
                }
            }
        }
        return resultList;
    }

    public static void displayResult(List<Result> resultList) {
        if (resultList == null || resultList.size() < 1) return;

        for (int i = 0; i < resultList.size(); i++) {
            Result result = resultList.get(i);
            System.out.println(String.format("(%s)ClassName: %s", (i + 1), result.className));
            for (Result.NameAndDesc item : result.list) {
                System.out.println(String.format("%s: %s", item.name, item.desc));
            }
            System.out.println();
        }
    }
}
