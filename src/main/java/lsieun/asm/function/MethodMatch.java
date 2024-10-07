package lsieun.asm.function;

@FunctionalInterface
public interface MethodMatch {
    boolean test(int version, String owner,
                 int methodAccess, String methodName, String methodDesc,
                 String signature, String[] exceptions);

    enum AllMethods implements MethodMatch {
        INSTANCE;

        @Override
        public boolean test(int version, String owner,
                            int methodAccess, String methodName, String methodDesc,
                            String signature, String[] exceptions) {
            return true;
        }
    }
}
