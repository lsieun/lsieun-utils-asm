package lsieun.asm.utils;

import java.io.IOException;
import java.io.PrintWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.TraceClassVisitor;

public class ASMPrint {
    public static void generate(String fqcn) {
        try {
            ASMifier printer = new ASMifier();
            TraceClassVisitor traceClassVisitor = new TraceClassVisitor(null, printer, new PrintWriter(System.out));
            new ClassReader(fqcn).accept(traceClassVisitor, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        generate("");
    }
}
