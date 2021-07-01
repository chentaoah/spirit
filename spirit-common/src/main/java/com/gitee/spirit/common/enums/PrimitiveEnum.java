package com.gitee.spirit.common.enums;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.hutool.core.lang.Assert;

public enum PrimitiveEnum {

    VOID("void", "void", "void", true, false), //
    BOOLEAN("boolean", "boolean", "boolean", true, false), //
    CHAR("char", "char", "char", true, false), //
    BYTE("byte", "byte", "byte", true, false), //
    SHORT("short", "short", "short", true, false), //
    INT("int", "int", "int", true, false), //
    LONG("long", "long", "long", true, false), //
    FLOAT("float", "float", "float", true, false), //
    DOUBLE("double", "double", "double", true, false), //

    BOOLEAN_ARRAY("[Z", "boolean[]", "boolean[]", false, true), //
    CHAR_ARRAY("[C", "char[]", "char[]", false, true), //
    BYTE_ARRAY("[B", "byte[]", "byte[]", false, true), //
    SHORT_ARRAY("[S", "short[]", "short[]", false, true), //
    INT_ARRAY("[I", "int[]", "int[]", false, true), //
    LONG_ARRAY("[J", "long[]", "long[]", false, true), //
    FLOAT_ARRAY("[F", "float[]", "float[]", false, true), //
    DOUBLE_ARRAY("[D", "double[]", "double[]", false, true); //

    public static final Map<String, PrimitiveEnum> CLASS_NAME_MAPPING = new ConcurrentHashMap<>();
    public static final Map<String, PrimitiveEnum> SIMPLE_NAME_MAPPING = new ConcurrentHashMap<>();
    public static final String PRIMITIVE_ENUM = "void|boolean|char|byte|short|int|long|float|double";

    static {
        for (PrimitiveEnum primitiveEnum : values()) {
            CLASS_NAME_MAPPING.put(primitiveEnum.className, primitiveEnum);
            SIMPLE_NAME_MAPPING.put(primitiveEnum.simpleName, primitiveEnum);
        }
        bindPrimitive(BOOLEAN, BOOLEAN_ARRAY);
        bindPrimitive(CHAR, CHAR_ARRAY);
        bindPrimitive(BYTE, BYTE_ARRAY);
        bindPrimitive(SHORT, SHORT_ARRAY);
        bindPrimitive(INT, INT_ARRAY);
        bindPrimitive(LONG, LONG_ARRAY);
        bindPrimitive(FLOAT, FLOAT_ARRAY);
        bindPrimitive(DOUBLE, DOUBLE_ARRAY);
    }

    public static void bindPrimitive(PrimitiveEnum primitive, PrimitiveEnum primitiveArray) {
        primitive.pointer = primitiveArray;
        primitiveArray.pointer = primitive;
    }

    public static boolean isPrimitive(String className) {
        return CLASS_NAME_MAPPING.containsKey(className) && CLASS_NAME_MAPPING.get(className).isPrimitive;
    }

    public static boolean isPrimitiveArray(String className) {
        return CLASS_NAME_MAPPING.containsKey(className) && CLASS_NAME_MAPPING.get(className).isArray;
    }

    public static boolean isPrimitiveBySimple(String simpleName) {
        return SIMPLE_NAME_MAPPING.containsKey(simpleName) && SIMPLE_NAME_MAPPING.get(simpleName).isPrimitive;
    }

    public static boolean isPrimitiveArrayBySimple(String simpleName) {
        return SIMPLE_NAME_MAPPING.containsKey(simpleName) && SIMPLE_NAME_MAPPING.get(simpleName).isArray;
    }

    public static String getTargetName(String className) {
        Assert.isTrue(isPrimitiveArray(className), "Class name must be a primitive array!");
        return CLASS_NAME_MAPPING.get(className).pointer.className;
    }

    public static String getArrayName(String className) {
        Assert.isTrue(isPrimitive(className), "Class name must be a primitive!");
        return CLASS_NAME_MAPPING.get(className).pointer.className;
    }

    public static String findClassName(String simpleName) {
        return SIMPLE_NAME_MAPPING.containsKey(simpleName) ? SIMPLE_NAME_MAPPING.get(simpleName).className : null;
    }

    public String className;
    public String simpleName;
    public String typeName;
    public boolean isPrimitive;
    public boolean isArray;
    public PrimitiveEnum pointer;

    private PrimitiveEnum(String className, String simpleName, String typeName, boolean isPrimitive, boolean isArray) {
        this.className = className;
        this.simpleName = simpleName;
        this.typeName = typeName;
        this.isPrimitive = isPrimitive;
        this.isArray = isArray;
    }

}
