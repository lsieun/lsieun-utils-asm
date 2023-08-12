package lsieun.asm.utils;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class ASMPrint {
    private static final int INPUT_STREAM_DATA_CHUNK_SIZE = 4096;

    public static void generate(String className, boolean asmCode) {
        int parsingOptions = ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG;
        generate(className, parsingOptions, asmCode);
    }

    public static void generate(String className, int parsingOptions, boolean asmCode) {
        try {
            String classFilePath = className.replace('.', '/') + ".class";
            InputStream in = ClassLoader.getSystemResourceAsStream(classFilePath);
            byte[] bytes = readStream(in, true);
            generate(bytes, parsingOptions, asmCode);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] readStream(final InputStream inputStream, final boolean close) throws IOException {
        if (inputStream == null) {
            throw new IOException("Class not found");
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] data = new byte[INPUT_STREAM_DATA_CHUNK_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                outputStream.write(data, 0, bytesRead);
            }
            outputStream.flush();
            return outputStream.toByteArray();
        }
        finally {
            if (close) {
                inputStream.close();
            }
        }
    }

    public static void generate(byte[] bytes, int parsingOptions, boolean asmCode) {
        // 第一步，创建ClassReader
        ClassReader cr = new ClassReader(bytes);

        // 第二步，创建ClassVisitor
        Printer printer = asmCode ? new ASMifier() : new Textifier();
        PrintWriter printWriter = new PrintWriter(System.out, true);
        ClassVisitor cv = new TraceClassVisitor(null, printer, printWriter);

        // 第三步，连接ClassReader和ClassVisitor
        cr.accept(cv, parsingOptions);
    }

    public static void main(String[] args) {
        generate("java.lang.Runnable", true);
    }
}
