package com.fa.ims.util;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class EnumUtils {
    private EnumUtils() {}
    public static <T> T getEnumByName(Class<T> clazz, String name) {
        return Arrays.stream(clazz.getEnumConstants()).filter(t -> t.toString().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
    public static <T> boolean isValidName(Class<T> clazz, String name) {
        return Arrays.stream(clazz.getEnumConstants()).anyMatch(t -> t.toString().equalsIgnoreCase(name));
    }

    public static <T> List<T> getEnumByNameContain(Class<T> clazz, String name) {
        return Arrays.stream(clazz.getEnumConstants()).filter(t -> t.toString().toLowerCase()
                                                                    .contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
}