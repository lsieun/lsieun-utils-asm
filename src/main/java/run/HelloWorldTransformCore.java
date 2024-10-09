package run;

import lsieun.utils.io.file.FileContentUtils;
import lsieun.utils.io.resource.ResourceUtils;

import java.io.IOException;
import java.nio.file.Path;

public class HelloWorldTransformCore {
    public static void main(String[] args) throws IOException {
        String relativePath = "sample/HelloWorld.class";
        Path path = ResourceUtils.readFilePath(relativePath);
        byte[] bytes1 = FileContentUtils.readBytes(path);
        byte[] bytes2 = null;
        FileContentUtils.writeBytes(path, bytes2);
    }
}