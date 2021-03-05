package lsieun.asm.utils;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ContainerUtils {
    public static String dir = "/home/liusen/Workspace/dummy/tmp/abc";
    public static Map<String, ContainerUtils> map = new HashMap<>();

    public static byte[] copy(byte[] bytes) {
        byte[] new_bytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            new_bytes[i] = bytes[i];
        }
        return new_bytes;
    }

    public static void start(ContainerUtils value) {
        String key = value.internalName;
        if (key == null) return;
        if (map.containsKey(key)) {
            System.out.println("start, Already contains Class: " + key);
            return;
        }

        map.put(key, value);
    }

    public static void stop(byte[] bytes, String key) {
        if (key == null) return;
        if (map.containsKey(key)) {
            System.out.println("stop, Do not contain Key: " + key);
            return;
        }
        ContainerUtils item = map.get(key);
        item.after_bytes = copy(bytes);
        item.dump();
    }


    public String internalName;
    public byte[] before_bytes;
    public byte[] after_bytes;

    public ContainerUtils(String internalName, byte[] before_bytes) {
        this.internalName = internalName;
        this.before_bytes = copy(before_bytes);
    }

    public void dump() {
        if (before_bytes == null || after_bytes == null) return;
        if (internalName == null) return;

        boolean equals = Arrays.equals(before_bytes, after_bytes);
        if (equals) return;

        if (Integer.valueOf(before_bytes.length).equals(Integer.valueOf(after_bytes.length))) return;
        System.out.println("a.length = " + before_bytes.length);
        System.out.println("b.length = " + after_bytes.length);

        String className = internalName.replace("/", ".");
        String filepath_a = String.format("%s/%s-a.class", dir, className);
        String filepath_b = String.format("%s/%s-b.class", dir, className);

        System.out.println(String.format("file://%s, length=%s", filepath_a, before_bytes.length));
        System.out.println(String.format("file://%s, length=%s", filepath_b, after_bytes.length));

        writeBytes(filepath_a, before_bytes);
        writeBytes(filepath_b, after_bytes);
    }

    public static void writeBytes(String filename, byte[] bytes) {
        File file = new File(filename);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (OutputStream out = new FileOutputStream(filename);
             BufferedOutputStream buff = new BufferedOutputStream(out);) {
            buff.write(bytes);
            buff.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
