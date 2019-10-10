package lsieun.asm.adapter.find;

import java.util.ArrayList;
import java.util.List;

public class Result {
    public final String className;
    public final List<NameAndDesc> list;

    public Result(String className) {
        this.className = className;
        this.list = new ArrayList<>();
    }

    public void add(String name, String desc) {
        NameAndDesc item = new NameAndDesc(name, desc);
        list.add(item);
    }

    public class NameAndDesc{
        public final String name;
        public final String desc;

        public NameAndDesc(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }
    }
}
