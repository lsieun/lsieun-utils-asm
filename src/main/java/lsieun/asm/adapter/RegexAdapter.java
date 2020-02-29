package lsieun.asm.adapter;

import lsieun.asm.adapter.find.Result;
import lsieun.asm.cst.Constant;
import lsieun.utils.RegexUtils;
import org.objectweb.asm.ClassVisitor;

import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.ACC_INTERFACE;

public class RegexAdapter extends ClassVisitor {
    // 开始前，传入的参数
    public final String[] includes;
    public final String[] excludes;

    // 过程中，记录的参数
    public String internalName;
    public boolean isInterface;

    // 结束后，输出的参数
    public boolean gotcha = false;
    public Result result;
    public List<Result> resultList;

    public RegexAdapter(ClassVisitor classVisitor, String[] includes, String[] excludes) {
        super(Constant.API_VERSION, classVisitor);
        this.includes = includes;
        this.excludes = excludes;
        resultList = new ArrayList<>();
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        // 先处理自己的逻辑
        internalName = name;
        isInterface = (access & ACC_INTERFACE) != 0;
        gotcha = false;
        result = null;

        // 再处理别人的逻辑
        super.visit(version, access, name, signature, superName, interfaces);
    }

    public Result getResult() {
        initFindResult();
        return result;
    }

    public void initFindResult() {
        if (result == null) {
            result = new Result(internalName);
        }
    }

    public boolean isTargetMember(String name, String descriptor) {
        String name_desc = String.format("%s%s%s", name, Constant.NAME_DESCRIPTOR_SEPARATOR, descriptor);
        boolean flag = isAppropriate(name_desc);
        if (flag) {
            gotcha = true;
            getResult().add(name, descriptor);
            return true;
        }
        return false;
    }

    public boolean isTargetInterface(String[] interfaces) {
        if (interfaces != null && interfaces.length > 0) {
            for (String item : interfaces) {
                boolean flag = isAppropriate(item);
                if (flag) {
                    gotcha = true;
                    initFindResult();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isTargetClassName(String superName) {
        boolean flag = isAppropriate(superName);
        if (flag) {
            gotcha = true;
            initFindResult();
            return true;
        }
        return false;
    }

    public boolean isAppropriate(String item) {
        if (RegexUtils.matches(item, excludes, false)) return false;
        if (RegexUtils.matches(item, includes, true)) return true;
        return false;
    }

}
