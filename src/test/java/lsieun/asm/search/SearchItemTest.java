package lsieun.asm.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SearchItemTest {

    @Test
    void testParse() {
        String str = "FIELD   java/lang/System  out:Ljava/io/PrintStream;";
        SearchItem searchItem = SearchItem.parse(str);
        System.out.println(searchItem);
    }
}