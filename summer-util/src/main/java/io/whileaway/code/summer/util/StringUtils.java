package io.whileaway.code.summer.util;

public interface StringUtils {

    String HYPHEN = "-";
    String UNDERSCORE = "_";

    static boolean isEmptyStr(String str) {
        return str == null || str.isEmpty() || str.isBlank();
    }

    static String pascalToUnderline(String str, String split) {
        if (isEmptyStr(str)) return "";
        StringBuilder builder = new StringBuilder(str);

        int index = 1;
        while (index < builder.length() - 1) {
            if (isUnderscoreRequired(builder.charAt(index - 1), builder.charAt(index), builder.charAt(index + 1))) {
//                builder.replace()
                builder.insert(index++, split);
            }
            ++index;
        }
        return builder.toString().toLowerCase();
    }

    private static boolean isUnderscoreRequired(char before, char current, char after) {
        return Character.isLowerCase(before) && Character.isUpperCase(current) && Character.isLowerCase(after);
    }
}
