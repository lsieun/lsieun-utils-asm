package lsieun.asm.utils;

import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import lsieun.asm.adapter.find.FindFieldAdapter;
import lsieun.asm.adapter.find.FindInterfaceAdapter;
import lsieun.asm.adapter.find.FindMethodAdaptor;
import lsieun.asm.adapter.find.FindSuperAdapter;
import lsieun.utils.RegexUtils;
import lsieun.utils.archive.JarUtils;

public class FindUtils {

    public static void findInterface(String jar_path,String[] path_regex_array, String[] interface_name_regex_array) {
        FindInterfaceAdapter cv = new FindInterfaceAdapter(null, interface_name_regex_array);
        find(jar_path, path_regex_array, cv);
    }

    public static void findSuper(String jar_path,String[] path_regex_array, String[] super_name_regex_array) {
        FindSuperAdapter cv = new FindSuperAdapter(null, super_name_regex_array);
        find(jar_path, path_regex_array, cv);
    }

    public static void findField(String jar_path,String[] path_regex_array, String[] name_desc_regex_array) {
        FindFieldAdapter cv = new FindFieldAdapter(null, name_desc_regex_array);
        find(jar_path, path_regex_array, cv);
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
     * @param path_regex_array path regex
     * @param name_desc_regex_array
     */
    public static void findMethod(String jar_path,String[] path_regex_array, String[] name_desc_regex_array) {
        FindMethodAdaptor cv = new FindMethodAdaptor(null, name_desc_regex_array);
        find(jar_path, path_regex_array, cv);
    }

    public static void find(String jar_path, String[] path_regex_array, ClassVisitor cv) {
        List<String> list = JarUtils.getClassEntries(jar_path);
        RegexUtils.filter(list, path_regex_array);

        for(String str : list) {
            byte[] bytes = JarUtils.readClass(jar_path, str);
            ClassReader cr = new ClassReader(bytes);
            cr.accept(cv, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        }
    }
}
