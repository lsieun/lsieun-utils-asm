package lsieun.asm.search;

import java.util.Objects;

public class SearchItem {
    public final SearchType type;

    public final String internalName;
    public final String name;
    public final String descriptor;

    private SearchItem(SearchType type, String internalName, String name, String descriptor) {
        this.type = type;
        this.internalName = internalName;
        this.name = name;
        this.descriptor = descriptor;
    }

    @Override
    public boolean equals(Object o) {
        // ref
        if (this == o) {
            return true;
        }

        // clazz
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        // fields
        SearchItem that = (SearchItem) o;
        return type == that.type &&
                Objects.equals(internalName, that.internalName) &&
                Objects.equals(name, that.name) &&
                Objects.equals(descriptor, that.descriptor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, internalName, name, descriptor);
    }

    @Override
    public String toString() {
        switch (type) {
            case CLASS: {
                return String.format("%s %s", type, internalName);
            }
            case FIELD:
            case METHOD:
            default: {
                return String.format("%s %s %s:%s", type, internalName, name, descriptor);
            }
        }
    }

    public static SearchItem ofType(String internalName) {
        return of(SearchType.CLASS, internalName, "", "");
    }

    public static SearchItem ofField(String internalName, String fieldName, String fieldDesc) {
        return of(SearchType.METHOD, internalName, fieldName, fieldDesc);
    }

    public static SearchItem ofMethod(String internalName, String methodName, String methodDesc) {
        return of(SearchType.METHOD, internalName, methodName, methodDesc);
    }

    public static SearchItem of(SearchType type, String internalName, String name, String descriptor) {
        return new SearchItem(type, internalName, name, descriptor);
    }

    public static SearchItem parse(String line) {
        if (line == null) {
            return null;
        }
        String[] array1 = line.split("\\s+");

        SearchType type = SearchType.valueOf(array1[0]);
        String internalName = array1[1];
        String name = "";
        String descriptor = "";
        if (type != SearchType.CLASS) {
            String[] array2 = array1[2].split(":");
            name = array2[0];
            descriptor = array2[1];
        }

        return of(type, internalName, name, descriptor);
    }
}
