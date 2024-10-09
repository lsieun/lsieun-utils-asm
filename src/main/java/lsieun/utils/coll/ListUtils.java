package lsieun.utils.coll;

import java.util.Collection;

public class ListUtils {
    public static boolean isNullOrEmpty(final Collection<?> c) {
        return c == null || c.isEmpty();
    }
}