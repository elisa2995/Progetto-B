package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Stringify {

    public static List<String> toString(List<?> list) {
        List<String> strings = new ArrayList<>();
        for (Object element : list) {
            strings.add(element.toString());
        }
        return strings;
    }

    public static <T1,T2> Map<String[], T2> toString(Map<T1[], T2> map) {
        Map<String[], T2> strings = new HashMap<>();
        for (Map.Entry<T1[], T2> row : map.entrySet()) {
            strings.put(toString(row.getKey()), row.getValue());
        }
        return strings;
    }

    public static <T> String[] toString(T[] array) {
        String[] strings = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            strings[i] = array[i].toString();
        }
        return strings;
    }
}
