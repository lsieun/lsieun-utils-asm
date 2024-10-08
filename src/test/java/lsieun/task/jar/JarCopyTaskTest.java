package lsieun.task.jar;

import lsieun.utils.log.LogLevel;
import lsieun.utils.log.Logger;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

class JarCopyTaskTest {
    @Test
    void testCopyJarByClass() throws IOException {
        Logger.CURRENT_LEVEL = LogLevel.DEBUG;
        Path dirPath = Path.of("D:\\service\\nexus-3.70.1-02");
        String[] classArray = {
                "javax.annotation.Nullable",
                "javax.servlet.ServletContext",
                "org.apache.karaf.features.Feature",
                "org.osgi.framework.Bundle",
                "org.sonatype.nexus.bootstrap.internal.DirectoryHelper",
                "org.slf4j.Logger",
                "com.sonatype.nexus.licensing.ext.NexusProfessionalFeature",
                "de.schlichtherle.license.LicenseContent",
                "javax.inject.Inject",
                "org.sonatype.licensing.CustomLicenseContent",
        };
        Path dstPath = Path.of("D:\\tmp\\nexus");
        JarCopyTask.copyJarByClass(dirPath, dstPath, classArray);
    }
}