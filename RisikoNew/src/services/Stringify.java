package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interface used to turn general objects or data structures into
 * strings/structures of strings.
 */
public interface Stringify {

    /**
     * Turns a List of Objects into a list of Strings. (Calls toString() on
     * each element of the array.)
     *
     * @param list
     * @return
     */
    public static List<String> toString(List<?> list) {
        List<String> strings = new ArrayList<>();
        for (Object element : list) {
            strings.add(element.toString());
        }
        return strings;
    }

    /**
     * Turns an array of objects into an array of strings. (Calls toString() on
     * each element of the array.)
     *
     * @param <T> a generic class that extends Object.
     * @param array
     * @return
     */
    public static <T> String[] toString(T[] array) {
        String[] strings = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            strings[i] = array[i].toString();
        }
        return strings;
    }

    /**
     * Turns the keys of a Map wihch have to be arrays of some class T1 into
     * arrays of Strings.
     *
     * @param <T1>
     * @param <T2>
     * @param map
     * @return
     */
    public static <T1, T2> Map<String[], T2> toString(Map<T1[], T2> map) {
        Map<String[], T2> strings = new HashMap<>();
        for (Map.Entry<T1[], T2> row : map.entrySet()) {
            strings.put(toString(row.getKey()), row.getValue());
        }
        return strings;
    }
}
