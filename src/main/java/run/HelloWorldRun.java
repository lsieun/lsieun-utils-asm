package run;

import org.objectweb.asm.Type;
import sample.HelloWorld;

import java.time.LocalDate;
import java.util.Map;

public class HelloWorldRun {
    public static void main(String[] args) throws Exception {
        HelloWorld instance = new HelloWorld();
//        String msg = instance.test("Tom", 10, LocalDate.now());
//        System.out.println(msg);
        instance.test();

        Type t = Type.getType(Map.Entry.class);
        System.out.println(t.getInternalName());
    }
}
