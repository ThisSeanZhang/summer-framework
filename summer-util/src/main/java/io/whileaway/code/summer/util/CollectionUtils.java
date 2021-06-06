package io.whileaway.code.summer.util;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public interface CollectionUtils {

    /**
     * Map
     */
    static boolean isEmpty(Map<?, ?> map) {
        return Objects.isNull(map) || map.size() == 0;
    }

    static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * Collection
     */
    static boolean isEmpty(Collection<?> list) {
        return Objects.isNull(list) || list.size() == 0;
    }

    static boolean isNotEmpty(Collection<?> list) {
        return !isEmpty(list);
    }

    /**
     * Array
     */
    static <T> boolean isEmpty(T[] array) {
        return Objects.isNull(array) || array.length == 0;
    }

    static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    /**
     * Byte Array
     */
    static boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    static boolean isNotEmpty(byte[] array) {
        return !isEmpty(array);
    }
}
