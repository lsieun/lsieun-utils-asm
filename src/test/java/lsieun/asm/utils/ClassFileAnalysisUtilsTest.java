package lsieun.asm.utils;

import lsieun.asm.function.match.MemberMatch;
import lsieun.asm.function.match.TextMatch;
import lsieun.utils.io.resource.ResourceUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

class ClassFileAnalysisUtilsTest {

    @Test
    void testListMembers() throws IOException {
        byte[] bytes = ResourceUtils.readClassBytes(Arrays.class);
        MemberMatch memberMatch = MemberMatch.byName(
                TextMatch.equals("toString")
        );
        ClassFileAnalysisUtils.listMembers(bytes, memberMatch);
    }

    @Test
    void analysis() {
    }
}