package com.gitee.spirit.common.pattern;

import java.util.regex.Pattern;

import com.gitee.spirit.common.enums.TokenTypeEnum;

public class CommonPattern {

    public static final Pattern ANNOTATION_PATTERN = Pattern.compile("^@[A-Z]+\\w+(\\([\\s\\S]+\\))?$");
    public static final Pattern SUBEXPRESS_PATTERN = Pattern.compile("^\\([\\s\\S]+\\)$");
    public static final Pattern VARIABLE_PATTERN = Pattern.compile("^[a-z]+\\w*$");
    public static final Pattern PREFIX_PATTERN = Pattern.compile("^(\\.)?\\w+$");

    public static boolean isAnnotation(String word) {
        return ANNOTATION_PATTERN.matcher(word).matches();
    }

    public static boolean isSubexpress(String word) {
        return SUBEXPRESS_PATTERN.matcher(word).matches();
    }

    public static boolean isVariable(String word) {
        return VARIABLE_PATTERN.matcher(word).matches();
    }

    public static boolean isPrefix(String word) {
        return PREFIX_PATTERN.matcher(word).matches();
    }

    public static TokenTypeEnum getSubexpressTokenType(String word) {
        if (isSubexpress(word)) {
            return TypePattern.isAnyType(CommonPattern.getSubexpressValue(word)) ? TokenTypeEnum.CAST : TokenTypeEnum.SUBEXPRESS;
        }
        return null;
    }

    public static String getAnnotationName(String word) {
        if (word.contains("(")) {
            word = word.substring(0, word.indexOf('('));
        }
        return word.substring(word.indexOf('@') + 1);
    }

    public static String getSubexpressValue(String word) {
        return word.substring(1, word.length() - 1);
    }

    public static String getPrefix(String word) {
        int start = word.startsWith(".") ? 1 : 0;
        int end = word.length();
        if (word.contains("{")) {
            int index = word.indexOf("{");
            end = Math.min(index, end);
        }
        if (word.contains("[")) {
            int index = word.indexOf("[");
            end = Math.min(index, end);
        }
        if (word.contains("(")) {
            int index = word.indexOf("(");
            end = Math.min(index, end);
        }
        return word.substring(start, end);
    }

}
