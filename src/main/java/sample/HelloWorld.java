package sample;

import java.lang.reflect.Constructor;

public class HelloWorld {
    public void test() throws Exception {
        Class<?> clazz = GoodChild.class;
        Constructor<?> constructor = clazz.getConstructor();
        Object instance = constructor.newInstance();
        clazz.getMethod("test").invoke(instance);
    }

    public void abc() {
        System.exit(1);
    }
}
