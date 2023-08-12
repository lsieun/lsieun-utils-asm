package lsieun.asm.visitor;

import lsieun.asm.cst.Constant;
import lsieun.utils.RegexUtils;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

public class ClassRegexVisitor extends ClassVisitor implements Opcodes {
    // 开始前，传入的参数
    public final String[] includes;
    public final String[] excludes;

    // 结束后，输出的数据
    public final List<String> resultList = new ArrayList<>();


    public ClassRegexVisitor(ClassVisitor cv, String[] includes, String[] excludes) {
        super(Constant.API_VERSION, cv);
        this.includes = includes;
        this.excludes = excludes;
    }

    public boolean isAppropriate(String item) {
        if (RegexUtils.matches(item, excludes, false)) {
            return false;
        }
        return RegexUtils.matches(item, includes, true);
    }

    protected String getMethodDescInfo(String methodName, String methodDesc) {
        return String.format("%s%s%s", methodName, Constant.COLON, methodDesc);
    }
}
