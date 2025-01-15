package dev.ash.techradar.domain.technology.enums;

class EnumTestUtils {

    static <E extends Enum<E>> boolean containsEnumValue(Class<E> enumClass, String value) {
        try {
            Enum.valueOf(enumClass, value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
