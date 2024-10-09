package lsieun.asm.utils;

import lsieun.asm.function.match.MemberMatch;
import lsieun.asm.function.match.TextMatch;
import lsieun.asm.function.match.TextMatchBuddy;
import lsieun.utils.io.resource.ResourceUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ClassFileAnalysisUtilsTest {

    @Test
    void testListMembers() throws IOException {
        byte[] bytes = ResourceUtils.readClassBytes(Arrays.class);
        MemberMatch memberMatch = MemberMatch.byName(
                TextMatchBuddy.equals("toString")
        );
        ClassFileAnalysisUtils.listMembers(bytes, memberMatch);
    }

    @Test
    void analysis() {
    }
}